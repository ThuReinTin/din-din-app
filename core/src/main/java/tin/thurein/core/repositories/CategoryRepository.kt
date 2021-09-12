package tin.thurein.core.repositories

import io.reactivex.Observable
import tin.thurein.core.models.Category

interface CategoryRepository {
    fun getCategories() : Observable<List<Category>>
}