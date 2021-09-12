package tin.thurein.dindin.viewmodels

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import tin.thurein.core.repositories.IngredientRepository
import tin.thurein.dindin.utils.addTo
import tin.thurein.dindin.utils.performBackOutOnMain
import tin.thurein.dindin.viewstates.IngredientViewState
import javax.inject.Inject

@HiltViewModel
class IngredientViewModel
@Inject constructor(
    private val ingredientRepository: IngredientRepository
) : BaseViewModel() {
    val viewState = MutableLiveData<IngredientViewState>()

    fun getIngredients(categoryId: Long, searchString: String?) {
        ingredientRepository.getIngredients(categoryId, searchString)
            .performBackOutOnMain()
            .subscribe(
                {
                    viewState.postValue(IngredientViewState.IngredientList(it))
                },
                {
                    viewState.postValue(IngredientViewState.Failure(it))
                }
            )
            .addTo(compositeDisposable)
    }
}