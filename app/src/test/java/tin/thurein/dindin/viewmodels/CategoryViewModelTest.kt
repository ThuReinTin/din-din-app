package tin.thurein.dindin.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.flextrade.jfixture.JFixture
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import tin.thurein.core.repositories.CategoryRepository
import tin.thurein.data.remote.CategoryService
import tin.thurein.data.repositories.CategoryRepositoryImpl
import tin.thurein.data.responses.CategoryResponse
import tin.thurein.data.responses.Response
import tin.thurein.data.responses.Status
import tin.thurein.dindin.viewstates.CategoryViewState

@RunWith(MockitoJUnitRunner::class)
class CategoryViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: CategoryViewModel

    @Mock
    lateinit var categoryService: CategoryService

    private val fixture = JFixture()

    private lateinit var categoryRepository: CategoryRepository

    @Before
    fun setUp() {
        categoryRepository = CategoryRepositoryImpl(categoryService)
        viewModel = CategoryViewModel(categoryRepository)
    }

    val EXPECTED_LOADING = 0
    val EXPECTED_SUCCESS = 1
    val EXPECTED_FAIILURE = 2

    val FAKE_SUCCESS = 1
    val FAKE_FAILURE = 2

    private val expectedViewState = mutableListOf<Int>()
    private var index = 0

    private val tag = this::class.java.name

    private val viewStateObserver = Observer<CategoryViewState> {
        when(expectedViewState[index]) {
            EXPECTED_LOADING -> {
                Assert.assertTrue("ViewState should be Loading", it is CategoryViewState.Loading)
            }
            EXPECTED_SUCCESS -> {
                Assert.assertTrue("ViewState should be CategoryList", it is CategoryViewState.CategoryList)
            }
            EXPECTED_FAIILURE -> {
                Assert.assertTrue("ViewState should be Failure", it is CategoryViewState.Failure)
            }
            else -> println("$tag : Unexpected ViewState $it")
        }
    }

    @Test
    fun getOrdersSuccessTest() {
        mockOrders(200, FAKE_SUCCESS, 4)

        expectedViewState.add(EXPECTED_LOADING)
        expectedViewState.add(EXPECTED_SUCCESS)

        viewModel.viewState.observeForever(viewStateObserver)

        viewModel.getCategories()
    }

    @Test
    fun getOrdersFailureTest() {
        mockOrders(201, FAKE_FAILURE )

        expectedViewState.add(EXPECTED_LOADING)
        expectedViewState.add(EXPECTED_FAIILURE)

        viewModel.viewState.observeForever(viewStateObserver)

        viewModel.getCategories()
    }

    private fun mockOrders(statusCode: Int, fakeInd: Int, size: Int = 0) {
        val orders = mutableListOf<CategoryResponse>()
        for (i in 0 until size) {
            orders.add(fixture.create(CategoryResponse::class.java))
        }
        val status = fixture.create(Status::class.java).copy(
            statusCode = statusCode
        )
        val response = Response(status, orders)

        Mockito.`when`(categoryService.getCategories()).then {
            when(fakeInd) {
                FAKE_SUCCESS -> {
                    Observable.just(response)
                }
                else -> {
                    Observable.error(Throwable("Fake Exception"))
                }
            }

        }
    }
}