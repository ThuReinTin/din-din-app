package tin.thurein.data.mappers

import tin.thurein.core.models.Category
import tin.thurein.data.responses.CategoryResponse

object CategoryMapper {
    fun CategoryResponse.map() : Category {
        return Category(
            id = this.id ?: -999,
            name = this.name ?: ""
        )
    }

    fun map(response: List<CategoryResponse>?) : MutableList<Category> {
        val list = mutableListOf<Category>()
        response?.let {
            for (categoryResponse in response) {
                list.add(categoryResponse.map())
            }
        }
        return list
    }
}