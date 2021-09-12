package tin.thurein.dindin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tin.thurein.core.models.Ingredient
import tin.thurein.dindin.R
import tin.thurein.dindin.databinding.IngredientLayoutBinding

class IngredientAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var ingredients = mutableListOf<Ingredient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = IngredientLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(ingredient = ingredients[position])
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun notifyData(list: List<Ingredient>) {
        ingredients.clear()
        ingredients.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: IngredientLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient) {
            if (ingredient.quantity <= 10) {
                binding.tvAvailable.setBackgroundResource(R.drawable.alert_available_background)
                binding.tvQuantity.setBackgroundResource(R.drawable.alert_quantity_background)
                binding.tvQuantity.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
            } else {
                binding.tvAvailable.setBackgroundResource(R.drawable.normal_available_background)
                binding.tvQuantity.setBackgroundResource(R.drawable.normal_quantity_background)
                binding.tvQuantity.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            }

            binding.tvTitle.text = ingredient.name
            binding.tvQuantity.text = ingredient.quantity.toString()

            Glide
                .with(binding.ivIngredient)
                .load(ingredient.imageUrl)
                .into(binding.ivIngredient)
        }
    }
}