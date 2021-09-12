package tin.thurein.data.responses

import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String
)