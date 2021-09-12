package tin.thurein.core.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient(
    var id: Long,
    var name: String,
    var quantity: Int,
    var imageUrl: String
) : Parcelable
