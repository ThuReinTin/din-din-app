package tin.thurein.data.mappers

import tin.thurein.core.models.Ingredient
import tin.thurein.data.responses.IngredientResponse

object IngredientMapper {
    fun IngredientResponse.map() : Ingredient {
        return Ingredient(
            id = this.id ?: -999,
            name = this.name ?: "",
            quantity = this.quantity ?: -1,
            imageUrl = imageUrl ?: ""
        )
    }

    fun map(response: List<IngredientResponse>?) : MutableList<Ingredient> {
        val list = mutableListOf<Ingredient>()
        response?.let {
            for (ingredientResponse in response) {
                list.add(ingredientResponse.map())
            }
        }
        return list
    }
}