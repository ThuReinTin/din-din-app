package tin.thurein.core.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Addon(
    var id: Long,
    var title: String,
    var quantity: Int,
    var protein: String,
    var almond: String,
    var moreEggs: String
) : Parcelable
