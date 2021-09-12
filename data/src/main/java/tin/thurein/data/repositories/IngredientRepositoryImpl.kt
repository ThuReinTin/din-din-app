package tin.thurein.data.repositories

import io.reactivex.Observable
import tin.thurein.core.models.Ingredient
import tin.thurein.core.repositories.IngredientRepository
import tin.thurein.data.mappers.IngredientMapper
import tin.thurein.data.remote.IngredientService
import javax.inject.Inject

class IngredientRepositoryImpl
    @Inject constructor(private val ingredientService: IngredientService) : IngredientRepository {
    override fun getIngredients(
        categoryId: Long,
        searchString: String?
    ): Observable<List<Ingredient>> {
        return ingredientService.getIngredients(categoryId, searchString)
            .flatMap {
                if (it.status.statusCode == 200) {
                    Observable.just(IngredientMapper.map(it.data))
                } else {
                    Observable.error(Throwable(it.status.message))
                }
            }
    }
}