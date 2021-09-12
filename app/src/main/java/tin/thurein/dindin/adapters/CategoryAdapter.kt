package tin.thurein.dindin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import tin.thurein.core.models.Category
import tin.thurein.dindin.fragments.IngredientFragment

class CategoryAdapter(fm: FragmentManager, private val categoryList: List<Category>) :
    FragmentPagerAdapter(fm) {
    override fun getCount(): Int = categoryList.size

    override fun getItem(position: Int): Fragment {
        return IngredientFragment.newInstance(categoryList[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categoryList[position].name
    }
}