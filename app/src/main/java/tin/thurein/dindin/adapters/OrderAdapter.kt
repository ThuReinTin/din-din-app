package tin.thurein.dindin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import tin.thurein.core.models.Order
import tin.thurein.dindin.R
import tin.thurein.dindin.databinding.OrderLayoutBinding
import tin.thurein.dindin.utils.DateTimeUtil
import tin.thurein.dindin.utils.DateTimeUtil.formatDate
import tin.thurein.dindin.utils.DateTimeUtil.parseToDate
import tin.thurein.dindin.utils.addTo
import tin.thurein.dindin.utils.performBackOutOnMain
import java.util.*
import java.util.concurrent.TimeUnit
import android.media.RingtoneManager
import android.util.Log
import androidx.core.content.ContextCompat
import tin.thurein.dindin.utils.performBackOutOnBack

class OrderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var orders = mutableListOf<Order>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = OrderLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(orders[position])
        }
    }

    override fun getItemCount(): Int = orders.size

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is ViewHolder) {
            holder.bind(orders[holder.bindingAdapterPosition])
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        if (holder is ViewHolder) {
            holder.unbind()
        }
        super.onViewDetachedFromWindow(holder)
    }

    fun notifyData(orders: List<Order>) {
        this.orders.clear()
        for (i in orders.indices) {
            this.orders.add(orders[i])
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: OrderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val addonAdapter = AddonAdapter()
        private val compositeDisposable = CompositeDisposable()

        private var isStarted = false

        init {
            binding.rvOrderDetail.layoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.VERTICAL, false
            )

            binding.rvOrderDetail.adapter = addonAdapter
            binding.rvOrderDetail.addItemDecoration(
                DividerItemDecoration(
                    binding.root.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        fun bind(order: Order) {
            val now = Date()
            updateLoadingView(-1)
            if (!isStarted && order.expiredAt.parseToDate()?.before(now) == false) {
                val diff = ((order.expiredAt.parseToDate()?.time ?: 0) - (now.time)) / 1000
                val duration = ((order.expiredAt.parseToDate()?.time
                    ?: 0) - (order.createdAt.parseToDate()?.time ?: 0)) / 1000

                countForProgress(diff, duration)
            }

            renderButton(order.expiredAt.parseToDate()?.before(Date()) ?: true)
            addonAdapter.notifyData(order.addonList)
            binding.tvOrderId.text = binding.root.context.getString(R.string.order_id, order.id)
            binding.tvOrderCreatedTime.text =
                order.createdAt.parseToDate()?.formatDate(DateTimeUtil.hh_mm_aa)
            binding.tvItemCount.text =
                binding.root.context.getString(R.string.n_items, order.addonList.size)
            binding.tvTimerDisplay.text = ""

            binding.btnExpire.setOnClickListener {
                compositeDisposable.clear()
                orders.removeAt(bindingAdapterPosition)
                notifyItemRemoved(bindingAdapterPosition)
            }

            binding.btnAccept.setOnClickListener {
                compositeDisposable.clear()
                orders.removeAt(bindingAdapterPosition)
                notifyItemRemoved(bindingAdapterPosition)
            }
        }

        fun unbind() {
            isStarted = false
            compositeDisposable.clear()
        }

        private fun renderButton(isExpired: Boolean) {
            binding.btnAccept.visibility = if (isExpired) View.GONE else View.VISIBLE
            binding.btnExpire.visibility = if (isExpired) View.VISIBLE else View.GONE
        }

        private fun countForProgress(countInSecond: Long, duration: Long) {
            isStarted = true
            Observable.interval(1, TimeUnit.SECONDS)
                .performBackOutOnMain()
                .takeUntil { it == countInSecond }
                .subscribe(
                    {
                        binding.tvTimerDisplay.text = getTime(countInSecond - it)
                        val diff = countInSecond - it
                        val divider = (diff * 100 / duration)
                        var res = -1
                        when {
                            divider >= 80 -> {
                                res = 5
                            }
                            divider >= 60 -> {
                                res = 4
                            }
                            divider >= 40 -> {
                                res = 3
                            }
                            divider >= 20 -> {
                                res = 2
                            }
                            divider >= 0 -> {
                                res = 1
                            }
                        }
                        updateLoadingView(res)
                    },
                    {
                        it.printStackTrace()
                    },
                    {
                        renderButton(true)
                        updateLoadingView(-1)
                    }
                )
                .addTo(compositeDisposable)
        }

        private fun getTime(seconds: Long): String {
            val weeks = seconds / 604800
            val days = seconds % 604800 / 86400
            val hours = seconds % 604800 % 86400 / 3600
            val minutes = seconds % 604800 % 86400 % 3600 / 60
            if (weeks > 0) {
                return if (weeks == 1L) {
                    "1 Week"
                } else {
                    "$weeks Weeks"
                }
            } else if (days > 0) {
                return if (days == 1L) {
                    "1 Day"
                } else {
                    "$days Days"
                }
            } else if (hours > 0) {
                return if (hours == 1L) {
                    "1 Hour"
                } else {
                    "$hours Hours"
                }
            } else if (minutes > 0) {
                return if (minutes == 1L) {
                    "1 Minute"
                } else {
                    "$minutes Minutes"
                }
            } else {
                return if (seconds == 1L) {
                    "1 Second"
                } else {
                    "$seconds Seconds"
                }
            }
        }

        private fun updateLoadingView(now: Int) {
            binding.vFifth.setBackgroundColor(
                ContextCompat.getColor(
                    binding.vFifth.context,
                    if (now > 0) R.color.red else
                        R.color.dark_gray
                )
            )
            binding.vFourth.setBackgroundColor(
                ContextCompat.getColor(
                    binding.vFifth.context,
                    if (now > 1) R.color.red else
                        R.color.dark_gray
                )
            )
            binding.vThird.setBackgroundColor(
                ContextCompat.getColor(
                    binding.vFifth.context,
                    if (now > 2) R.color.red else
                        R.color.dark_gray
                )
            )
            binding.vSecond.setBackgroundColor(
                ContextCompat.getColor(
                    binding.vFifth.context,
                    if (now > 3) R.color.red else
                        R.color.dark_gray
                )
            )

            binding.vFirst.setBackgroundColor(
                ContextCompat.getColor(
                    binding.vFifth.context,
                    if (now > 4) R.color.red else
                        R.color.dark_gray
                )
            )

            binding.llLoading.visibility = if (now < 0 || now > 5) View.GONE else View.VISIBLE
        }
    }
}