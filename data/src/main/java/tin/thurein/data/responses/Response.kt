package tin.thurein.data.responses

import com.google.gson.annotations.SerializedName

data class Response<T>(
    @SerializedName("status") val status: Status,
    @SerializedName("data") val data: T?
)
