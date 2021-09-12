package tin.thurein.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import tin.thurein.data.responses.IngredientResponse
import tin.thurein.data.responses.Response

interface IngredientService {
    @GET("getIngredients")
    fun getIngredients(
        @Query("categoryId") categoryId: Long,
        @Query("searchString") searchString: String?
    ): Observable<Response<List<IngredientResponse>>>
}