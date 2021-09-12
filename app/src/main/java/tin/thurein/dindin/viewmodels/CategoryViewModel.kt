package tin.thurein.dindin.viewmodels

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import tin.thurein.core.repositories.CategoryRepository
import tin.thurein.dindin.utils.addTo
import tin.thurein.dindin.utils.performBackOutOnMain
import tin.thurein.dindin.viewstates.CategoryViewState
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel
@Inject constructor(private val categoryRepository: CategoryRepository) : BaseViewModel() {
    val viewState = MutableLiveData<CategoryViewState>()

    fun getCategories() {
        categoryRepository.getCategories()
            .performBackOutOnMain()
            .subscribe(
                {
                    viewState.postValue(CategoryViewState.CategoryList(it))
                },
                {
                    viewState.postValue(CategoryViewState.Failure(it))
                }
            )
            .addTo(compositeDisposable)
    }
}