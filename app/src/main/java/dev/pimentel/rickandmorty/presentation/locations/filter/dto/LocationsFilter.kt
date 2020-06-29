package dev.pimentel.rickandmorty.presentation.locations.filter.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationsFilter(
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null
) : Parcelable {

    companion object {
        val NO_FILTER = LocationsFilter()
    }
}
