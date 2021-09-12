package tin.thurein.dindin.rxbus

import tin.thurein.core.models.Order

data class OrderActionEvent(
    val isExpired: Boolean,
    val order: Order,
    val position: Int
)