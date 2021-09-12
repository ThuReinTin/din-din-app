package tin.thurein.dindin.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import com.hwangjr.rxbus.RxBus
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import tin.thurein.core.models.Category
import tin.thurein.dindin.adapters.CategoryAdapter
import tin.thurein.dindin.databinding.ActivityCategoryBinding
import tin.thurein.dindin.delegate.viewBinding
import tin.thurein.dindin.rxbus.SearchIngredientEvent
import tin.thurein.dindin.utils.addTo
import tin.thurein.dindin.viewmodels.CategoryViewModel
import tin.thurein.dindin.viewstates.CategoryViewState
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CategoryActivity : BaseActivity() {
    private val binding by viewBinding(ActivityCategoryBinding::inflate)

    private lateinit var ingredientPager : CategoryAdapter

    private val viewModel : CategoryViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            hideSoftKeyboard(it)
            onBackPressed()
        }

        RxTextView.textChangeEvents(binding.etSearch)
            .skip(1)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    RxBus.get().post(SearchIngredientEvent(it.text().toString()))
                },
                {
                    it.printStackTrace()
                }
            ).addTo(compositeDisposable)

        viewModel.viewState.observe(this, this::render)
        viewModel.getCategories()
    }

    private fun render(viewState: CategoryViewState) {
        when(viewState) {
            is CategoryViewState.Loading -> {
                Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            }
            is CategoryViewState.CategoryList -> {
                renderPager(viewState.data)
            }
            is CategoryViewState.Failure -> {
                Toast.makeText(this, viewState.e.message ?: "Unknown error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderPager(categoryList: List<Category>) {
        ingredientPager = CategoryAdapter(
            supportFragmentManager,
            categoryList
        )

        binding.tlCategory.setupWithViewPager(binding.vpIngredient)
        binding.vpIngredient.adapter = ingredientPager
    }

    private fun hideSoftKeyboard(view: View) {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}