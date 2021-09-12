package tin.thurein.data.repositories

import io.reactivex.Observable
import tin.thurein.core.models.Order
import tin.thurein.core.repositories.OrderRepository
import tin.thurein.data.mappers.OrderMapper
import tin.thurein.data.remote.OrderService
import javax.inject.Inject

class OrderRepositoryImpl
    @Inject constructor(private val orderService: OrderService)
    : OrderRepository {
    override fun getOrders(): Observable<MutableList<Order>> {
        return orderService.getOrders()
            .flatMap {
                if (it.status.statusCode == 200) {
                    Observable.just(OrderMapper.map(it.data))
                } else {
                    Observable.error(Throwable(it.status.message))
                }
            }
    }
}