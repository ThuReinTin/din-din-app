package tin.thurein.data.responses

import com.google.gson.annotations.SerializedName

data class AddonResponse(
    @SerializedName("id") val id: Long?,
    @SerializedName("title") val title: String?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("protein") val protein: String?,
    @SerializedName("almond") val almond: String?,
    @SerializedName("moreEggs") val moreEggs: String?
)
