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
import tin.thurein.core.repositories.IngredientRepository
import tin.thurein.data.remote.IngredientService
import tin.thurein.data.repositories.IngredientRepositoryImpl
import tin.thurein.data.responses.IngredientResponse
import tin.thurein.data.responses.Response
import tin.thurein.data.responses.Status
import tin.thurein.dindin.viewstates.IngredientViewState

@RunWith(MockitoJUnitRunner::class)
class IngredientViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: IngredientViewModel

    @Mock
    lateinit var categoryService: IngredientService

    private val fixture = JFixture()

    private lateinit var categoryRepository: IngredientRepository

    @Before
    fun setUp() {
        categoryRepository = IngredientRepositoryImpl(categoryService)
        viewModel = IngredientViewModel(categoryRepository)
    }

    val EXPECTED_LOADING = 0
    val EXPECTED_SUCCESS = 1
    val EXPECTED_FAIILURE = 2

    val FAKE_SUCCESS = 1
    val FAKE_FAILURE = 2

    private val expectedViewState = mutableListOf<Int>()
    private var index = 0

    private val tag = this::class.java.name

    private val viewStateObserver = Observer<IngredientViewState> {
        when(expectedViewState[index]) {
            EXPECTED_LOADING -> {
                Assert.assertTrue("ViewState should be Loading", it is IngredientViewState.Loading)
            }
            EXPECTED_SUCCESS -> {
                Assert.assertTrue("ViewState should be IngredientList", it is IngredientViewState.IngredientList)
            }
            EXPECTED_FAIILURE -> {
                Assert.assertTrue("ViewState should be Failure", it is IngredientViewState.Failure)
            }
            else -> println("$tag : Unexpected ViewState $it")
        }
    }

    @Test
    fun getOrdersSuccessTest() {
        val categoryId = 2L
        val searchString = ""
        mockOrders(categoryId, searchString, 200, FAKE_SUCCESS, 4)

        expectedViewState.add(EXPECTED_LOADING)
        expectedViewState.add(EXPECTED_SUCCESS)

        viewModel.viewState.observeForever(viewStateObserver)

        viewModel.getIngredients(categoryId, searchString)
    }

    @Test
    fun getOrdersFailureTest() {
        val categoryId = 3L
        val searchString = "search"
        mockOrders(categoryId, searchString, 201, FAKE_FAILURE )

        expectedViewState.add(EXPECTED_LOADING)
        expectedViewState.add(EXPECTED_FAIILURE)

        viewModel.viewState.observeForever(viewStateObserver)

        viewModel.getIngredients(categoryId, searchString)
    }

    private fun mockOrders(categoryId: Long, searchString: String, statusCode: Int, fakeInd: Int, size: Int = 0) {
        val orders = mutableListOf<IngredientResponse>()
        for (i in 0 until size) {
            orders.add(fixture.create(IngredientResponse::class.java))
        }
        val status = fixture.create(Status::class.java).copy(
            statusCode = statusCode
        )
        val response = Response(status, orders)

        Mockito.`when`(categoryService.getIngredients(categoryId, searchString)).then {
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