package tin.thurein.core.repositories

import io.reactivex.Observable
import tin.thurein.core.models.Ingredient

interface IngredientRepository {
    fun getIngredients(categoryId: Long, searchString: String?) : Observable<List<Ingredient>>
}