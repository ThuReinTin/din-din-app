package tin.thurein.core.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(val id: Long, val name: String) : Parcelable