package tin.thurein.data.responses

import com.google.gson.annotations.SerializedName

data class IngredientResponse(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("image_url") val imageUrl: String?
)
