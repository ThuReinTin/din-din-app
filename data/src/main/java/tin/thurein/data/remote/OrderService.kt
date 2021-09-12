package tin.thurein.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import tin.thurein.data.responses.OrderResponse
import tin.thurein.data.responses.Response


interface OrderService {
    @GET("getOrders")
    fun getOrders(): Observable<Response<List<OrderResponse>>>
}