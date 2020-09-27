package dev.pimentel.rickandmorty.presentation.locations.dto

import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter

sealed class LocationsIntent {

    data class GetLocations(val filter: LocationsFilter) : LocationsIntent()

    object GetLocationsWithLastFilter : LocationsIntent()

    object OpenFilters : LocationsIntent()
}
