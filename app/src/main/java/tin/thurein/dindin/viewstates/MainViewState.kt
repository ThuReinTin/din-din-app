package tin.thurein.dindin.viewstates

import tin.thurein.core.models.Order

sealed class MainViewState {
    object Loading : MainViewState()
    data class OrderList(var data: MutableList<Order>) : MainViewState()
    data class Failure(val e: Throwable) : MainViewState()
}