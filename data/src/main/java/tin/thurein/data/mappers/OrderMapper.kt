package tin.thurein.data.mappers

import tin.thurein.core.models.Order
import tin.thurein.data.responses.OrderResponse

object OrderMapper {
    fun OrderResponse.map() : Order {
        return Order(
            id = this.id ?: -999,
            title = this.title ?: "",
            quantity = this.quantity ?: -1,
            createdAt = this.createdAt ?: "",
            alertedAt = this.alertedAt ?: "",
            expiredAt = this.expiredAt ?: "",
            addonList = AddonMapper.map(this.addonList)
        )
    }

    fun map(response: List<OrderResponse>?) : MutableList<Order> {
        val list = mutableListOf<Order>()
        response?.let {
            for (orderResponse in response) {
                list.add(orderResponse.map())
            }
        }
        return list
    }
}