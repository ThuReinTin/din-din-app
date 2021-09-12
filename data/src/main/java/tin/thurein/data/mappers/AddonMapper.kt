package tin.thurein.data.mappers

import tin.thurein.core.models.Addon
import tin.thurein.data.responses.AddonResponse

object AddonMapper {
    fun AddonResponse.map() : Addon {
        return Addon(
            id = id ?: -999,
            title = title ?: "",
            quantity = quantity ?: -99,
            protein = protein ?: "",
            almond = almond ?: "",
            moreEggs = moreEggs ?: ""
        )
    }

    fun map(response: List<AddonResponse>?) : MutableList<Addon> {
        val list = mutableListOf<Addon>()
        response?.let {
            for (addonResponse in response) {
                list.add(addonResponse.map())
            }
        }
        return list
    }
}