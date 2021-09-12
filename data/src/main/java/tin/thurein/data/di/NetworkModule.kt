package tin.thurein.data.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory
import tin.thurein.data.remote.mockers.MockInterceptor as Mocker

import retrofit2.Retrofit

import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Qualifier

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import okhttp3.*
import okio.Buffer
import tin.thurein.data.BuildConfig
import tin.thurein.data.exceptions.NetworkException
import tin.thurein.data.remote.CategoryService
import tin.thurein.data.remote.IngredientService
import tin.thurein.data.remote.OrderService
import java.lang.Exception
import java.nio.charset.Charset


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @NetworkInterceptor
    fun provideNetworkInterceptor(context: Application) = Interceptor {
        val request = it.request()
        if (!isNetworkAvailable(context)) {
            throw NetworkException()
        }
        it.proceed(request)
    }

    @Provides
    @LogInterceptor
    fun provideLogInterceptor() = Interceptor { chain ->
        val request = chain.request()

        try {
            val response = chain.proceed(request)

            val endpoint =
                request.url.toString().substring(request.url.toString().lastIndexOf("/") + 1)
            val source = response.body?.source()
            source?.request(Long.MAX_VALUE)
            val buffer = source?.buffer
            val responseStr = buffer?.clone()?.readString(Charset.forName("UTF-8")) ?: "No response"
            Log.e(
                "LogInterceptor",
                "$endpoint : ${requestBodyString(request = request.body)} : $responseStr"
            )
            response
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("provideLogInterceptor", "${ex}")
            chain.proceed(request)
        }
    }

    @Provides
    @MockInterceptor
    fun provideMockInterceptor() : Interceptor = Mocker()

    @Provides
    fun provideOkHttpClient(
        @NetworkInterceptor networkInterceptor: Interceptor,
        @LogInterceptor logInterceptor: Interceptor,
        @MockInterceptor mockInterceptor: Interceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .addInterceptor(logInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(mockInterceptor)
        }
        return clientBuilder.build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideOrderService(
        retrofit: Retrofit
    ): OrderService {
        return retrofit.create(OrderService::class.java)
    }

    @Provides
    fun provideCategoryService(
        retrofit: Retrofit
    ): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }

    @Provides
    fun provideIngredientService(
        retrofit: Retrofit
    ): IngredientService {
        return retrofit.create(IngredientService::class.java)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)
            return connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    )
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                return (
                        activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                                activeNetwork.type == ConnectivityManager.TYPE_MOBILE
                        )
            }
            return false
        }
    }

    private fun requestBodyString(request: RequestBody?): String {
        val buffer = Buffer()
        if (request != null)
            request.writeTo(buffer)
        else
            return ""
        return buffer.readUtf8()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NetworkInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LogInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockInterceptor