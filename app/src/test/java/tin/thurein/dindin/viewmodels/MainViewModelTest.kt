package tin.thurein.dindin.viewmodels

import android.util.Log
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
import tin.thurein.core.models.Order
import tin.thurein.core.repositories.OrderRepository
import tin.thurein.data.remote.OrderService
import tin.thurein.data.repositories.OrderRepositoryImpl
import tin.thurein.data.responses.OrderResponse
import tin.thurein.data.responses.Response
import tin.thurein.data.responses.Status
import tin.thurein.dindin.viewstates.MainViewState

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: MainViewModel

    @Mock
    lateinit var orderService: OrderService

    private val fixture = JFixture()

    private lateinit var orderRepository: OrderRepository

    @Before
    fun setUp() {
        orderRepository = OrderRepositoryImpl(orderService)
        viewModel = MainViewModel(orderRepository)
    }

    val EXPECTED_LOADING = 0
    val EXPECTED_SUCCESS = 1
    val EXPECTED_FAIILURE = 2

    val FAKE_SUCCESS = 1
    val FAKE_FAILURE = 2

    private val expectedViewState = mutableListOf<Int>()
    private var index = 0

    private val tag = this::class.java.name

    private val viewStateObserver = Observer<MainViewState> {
        when(expectedViewState[index]) {
            EXPECTED_LOADING -> {
                Assert.assertTrue("ViewState should be Loading", it is MainViewState.Loading)
            }
            EXPECTED_SUCCESS -> {
                Assert.assertTrue("ViewState should be OrderList", it is MainViewState.OrderList)
            }
            EXPECTED_FAIILURE -> {
                Assert.assertTrue("ViewState should be Failure", it is MainViewState.Failure)
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

        viewModel.getOrders()
    }

    @Test
    fun getOrdersFailureTest() {
        mockOrders(201, FAKE_FAILURE )

        expectedViewState.add(EXPECTED_LOADING)
        expectedViewState.add(EXPECTED_FAIILURE)

        viewModel.viewState.observeForever(viewStateObserver)

        viewModel.getOrders()
    }

    private fun mockOrders(statusCode: Int, fakeInd: Int, size: Int = 0) {
        val orders = mutableListOf<OrderResponse>()
        for (i in 0 until size) {
            orders.add(fixture.create(OrderResponse::class.java))
        }
        val status = fixture.create(Status::class.java).copy(
            statusCode = statusCode
        )
        val response = Response(status, orders)

        Mockito.`when`(orderService.getOrders()).then {
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