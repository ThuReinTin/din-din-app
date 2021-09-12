package tin.thurein.dindin.viewmodels

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import tin.thurein.core.repositories.OrderRepository
import tin.thurein.dindin.viewstates.MainViewState
import tin.thurein.dindin.utils.addTo
import tin.thurein.dindin.utils.performBackOutOnMain
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val orderRepository: OrderRepository) : BaseViewModel() {

    val viewState = MutableLiveData<MainViewState>()
    fun getOrders() {
        viewState.postValue(MainViewState.Loading)
        orderRepository.getOrders()
            .performBackOutOnMain()
            .subscribe(
                {
                    viewState.postValue(MainViewState.OrderList(it))
                },
                {
                    viewState.postValue(MainViewState.Failure(it))
                }
            ).addTo(compositeDisposable)
    }
}