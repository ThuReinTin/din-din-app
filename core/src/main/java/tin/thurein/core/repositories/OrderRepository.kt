package tin.thurein.core.repositories

import io.reactivex.Observable
import tin.thurein.core.models.Order

interface OrderRepository {
    fun getOrders() : Observable<MutableList<Order>>
}