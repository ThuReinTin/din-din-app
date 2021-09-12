package tin.thurein.dindin.viewstates

import tin.thurein.core.models.Ingredient

sealed class IngredientViewState {
    object Loading : IngredientViewState()
    data class IngredientList(val ingredients: List<Ingredient>) : IngredientViewState()
    data class Failure(val e: Throwable) : IngredientViewState()
}
