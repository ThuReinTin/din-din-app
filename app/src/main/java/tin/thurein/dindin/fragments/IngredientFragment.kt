package tin.thurein.dindin.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.hwangjr.rxbus.annotation.Subscribe
import dagger.hilt.android.AndroidEntryPoint
import tin.thurein.core.models.Category
import tin.thurein.dindin.adapters.IngredientAdapter
import tin.thurein.dindin.databinding.FragmentIngredientBinding
import tin.thurein.dindin.rxbus.SearchIngredientEvent
import tin.thurein.dindin.viewmodels.IngredientViewModel
import tin.thurein.dindin.viewstates.IngredientViewState

private const val CATEGORY = "param1"

@AndroidEntryPoint
class IngredientFragment : BaseFragment() {

    private var category: Category? = null

    private var _binding: FragmentIngredientBinding? = null

    private val binding get() = _binding!!

    private val viewModel : IngredientViewModel by viewModels()

    private val adapter: IngredientAdapter = IngredientAdapter()

    private var searchString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(CATEGORY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner, this::render)

        binding.rvIngredient.layoutManager = GridLayoutManager(context, 5)
        binding.rvIngredient.adapter = adapter

        binding.srlIngredient.setOnRefreshListener {
            getIngredients()
        }

        getIngredients()
    }

    private fun render(viewState: IngredientViewState) {
        when(viewState) {
            is IngredientViewState.Loading -> {
                Toast.makeText(context,  "Loading...", Toast.LENGTH_SHORT).show()
                binding.srlIngredient.isRefreshing = true
            }
            is IngredientViewState.IngredientList -> {
                adapter.notifyData(viewState.ingredients)
                hideSoftKeyboard(binding.root)
                binding.srlIngredient.isRefreshing = false
            }
            is IngredientViewState.Failure -> {
                Toast.makeText(context, viewState.e.message ?: "Unknown error occurred", Toast.LENGTH_SHORT).show()
                binding.srlIngredient.isRefreshing = false
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(category: Category) =
            IngredientFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY, category)
                }
            }
    }

    @Subscribe
    fun listenSearchTextChange(event: SearchIngredientEvent) {
        if (event.searchString.isNotBlank()) {
            searchString = event.searchString
        } else {
            searchString = null
        }
        getIngredients()
    }

    private fun getIngredients() {
        arguments?.let {
            viewModel.getIngredients(it.getParcelable<Category>(CATEGORY)?.id ?: -1, searchString)
        }
    }

    private fun hideSoftKeyboard(view: View) {
        val imm: InputMethodManager? =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}