package tin.thurein.data.responses

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?
)
