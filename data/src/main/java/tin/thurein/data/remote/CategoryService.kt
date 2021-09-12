package tin.thurein.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import tin.thurein.data.responses.CategoryResponse
import tin.thurein.data.responses.Response

interface CategoryService {
    @GET("getCategories")
    fun getCategories(): Observable<Response<List<CategoryResponse>>>
}