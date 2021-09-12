package tin.thurein.data.responses

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("id") val id: Long?,
    @SerializedName("title") val title: String?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("alerted_at") val alertedAt: String?,
    @SerializedName("expired_at") val expiredAt: String?,
    @SerializedName("addon") val addonList: MutableList<AddonResponse>?
)