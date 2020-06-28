package dev.pimentel.rickandmorty.presentation.locations.filter.dto

data class LocationsFilter(
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null
) {

    companion object {
        val NO_FILTER = LocationsFilter()
    }
}
