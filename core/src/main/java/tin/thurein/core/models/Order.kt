package tin.thurein.core.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    var id: Long,
    var title: String,
    var quantity: Int,
    var createdAt: String,
    var alertedAt: String,
    var expiredAt: String,
    var addonList: MutableList<Addon> = mutableListOf()
    ) : Parcelable
