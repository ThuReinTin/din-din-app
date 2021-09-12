package tin.thurein.dindin.activities

import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import tin.thurein.core.models.Order
import tin.thurein.dindin.R
import tin.thurein.dindin.viewstates.MainViewState
import tin.thurein.dindin.adapters.OrderAdapter
import tin.thurein.dindin.databinding.ActivityMainBinding
import tin.thurein.dindin.delegate.viewBinding
import tin.thurein.dindin.utils.DateTimeUtil.parseToDate
import tin.thurein.dindin.utils.addTo
import tin.thurein.dindin.utils.performBackOutOnBack
import tin.thurein.dindin.viewmodels.MainViewModel
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    private lateinit var timer : CountDownTimer

    private val orderDetailAdaper = OrderAdapter()

    private val mainViewModel: MainViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mainViewModel.viewState.observe (this, this::renderOrders)

        val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvOrders.layoutManager = layoutManager
        binding.rvOrders.adapter = orderDetailAdaper

        mainViewModel.getOrders()

        binding.ivNext.setOnClickListener {
            (binding.rvOrders.layoutManager as LinearLayoutManager).scrollToPosition(layoutManager.findLastVisibleItemPosition() + 1)
        }

        binding.rvOrders.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.ivNext.visibility = View.VISIBLE
                if (layoutManager.findLastVisibleItemPosition() == orderDetailAdaper.orders.size - 1) {
                    binding.ivNext.visibility = View.GONE
                }
            }
        })

        binding.ivIngredient.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        binding.srlOrders.setOnRefreshListener {
            mainViewModel.getOrders()
        }

    }

    private fun renderOrders(viewState: MainViewState) {
        when(viewState) {
            is MainViewState.Loading -> {
                compositeDisposable.clear()
                binding.srlOrders.isRefreshing = true
                Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            }
            is MainViewState.Failure -> {
                binding.srlOrders.isRefreshing = false
                Toast.makeText(this, viewState.e.message ?: "Unknown error occurred", Toast.LENGTH_SHORT).show()
            }
            is MainViewState.OrderList -> {
                render(viewState.data)
            }
        }
    }

    private fun render(orders: MutableList<Order>) {
        binding.srlOrders.isRefreshing = false
        orderDetailAdaper.notifyData(orders)
        binding.tvOrderCount.text = getString(R.string.order_count, orders.size)
        val now = Date()
        for (order in orders) {
            if (order.expiredAt.parseToDate()?.before(now) == false) {
                val alerted = ((order.alertedAt.parseToDate()?.time ?: 0) - (now.time)) / 1000
                Log.e("Alerted", "${order.alertedAt.parseToDate()}")
                countForAlert(alertedAt = alerted)
            }
        }
    }

    private fun countForAlert(alertedAt: Long) {
        Observable.interval(1, TimeUnit.SECONDS)
            .performBackOutOnBack()
            .takeUntil { it == alertedAt }
            .subscribe(
                { Log.d("Counting for alert", "$it") },
                { it.printStackTrace() },
                { playNotificationSound() }
            )
            .addTo(compositeDisposable)
    }

    private fun playNotificationSound() {
        RingtoneManager.getRingtone(
            binding.root.context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        )?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}