package tin.thurein.data.repositories

import io.reactivex.Observable
import tin.thurein.core.models.Category
import tin.thurein.core.repositories.CategoryRepository
import tin.thurein.data.mappers.CategoryMapper
import tin.thurein.data.remote.CategoryService
import javax.inject.Inject

class CategoryRepositoryImpl
    @Inject constructor(private val categoryService: CategoryService) : CategoryRepository {
    override fun getCategories(): Observable<List<Category>> {
        return categoryService.getCategories()
            .flatMap {
                if (it.status.statusCode == 200) {
                    Observable.just(CategoryMapper.map(it.data))
                } else {
                    Observable.error(Throwable(it.status.message))
                }
            }
    }
}