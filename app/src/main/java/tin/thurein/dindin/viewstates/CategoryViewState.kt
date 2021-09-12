package tin.thurein.dindin.viewstates

import tin.thurein.core.models.Category

sealed class CategoryViewState {
    object Loading : CategoryViewState()
    data class CategoryList(var data: List<Category>) : CategoryViewState()
    data class Failure(val e: Throwable) : CategoryViewState()
}