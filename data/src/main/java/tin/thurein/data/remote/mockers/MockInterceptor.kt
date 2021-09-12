package tin.thurein.data.remote.mockers

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import tin.thurein.data.BuildConfig

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()
            val responseString = when {
                uri.contains("getOrders") -> {
                   OrderMocker.getOrders()
                }
                uri.contains("getCategories") -> {
                    CategoryMocker.getCategory()
                }
                uri.contains("getIngredients") && uri.contains("search") -> {
                    IngredientMocker.searchIngredient()
                }
                uri.contains("getIngredients") -> {
                    IngredientMocker.getIngredient()
                }
                else -> ""
            }

            return if (responseString.isNotEmpty()) {
                chain.proceed(chain.request())
                    .newBuilder()
                    .code(200)
                    .protocol(Protocol.HTTP_1_1)
                    .message(responseString)
                    .body(
                        responseString.toByteArray()
                            .toResponseBody("application/json".toMediaTypeOrNull())
                    )
                    .addHeader("content-type", "application/json")
                    .build()
            } else {
                chain.proceed(chain.request())
            }
        } else {
            // just to be on safe side.
            throw IllegalAccessError(
                "MockInterceptor is only meant for Testing Purposes and " +
                        "bound to be used only with DEBUG mode"
            )
        }
    }
}