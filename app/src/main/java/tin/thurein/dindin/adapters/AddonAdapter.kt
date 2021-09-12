package tin.thurein.dindin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tin.thurein.core.models.Addon
import tin.thurein.dindin.R
import tin.thurein.dindin.databinding.AddonLayoutBinding

class AddonAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var addons = mutableListOf<Addon>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = AddonLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(addons[position])
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is ViewHolder) {
            holder.unbind()
        }
    }

    override fun getItemCount(): Int = addons.size

    fun notifyData(list: List<Addon>) {
        addons.clear()
        addons.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AddonLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(addon: Addon) {
            if (addon.protein.isNotBlank())  {
                binding.tvHash.text = addon.protein
            } else {
                binding.tvHash.visibility = View.GONE
            }

            if (addon.almond.isNotBlank())  {
                binding.tvAlmond.text = addon.almond
            } else {
                binding.tvAlmond.visibility = View.GONE
            }

            if (addon.moreEggs.isNotBlank())  {
                binding.tvMoreEgg.text = addon.moreEggs
            } else {
                binding.tvMoreEgg.visibility = View.GONE
            }

            binding.tvTitle.text = binding.root.context.getString(R.string.addon_quantity_title, addon.quantity, addon.title)
        }

        fun unbind() {}
    }
}