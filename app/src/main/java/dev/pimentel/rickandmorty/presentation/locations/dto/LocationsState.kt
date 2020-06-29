package dev.pimentel.rickandmorty.presentation.locations.dto

sealed class LocationsState(
    val locations: List<LocationsItem> = emptyList(),
    val errorMessage: String? = null
) {

    class Empty() : LocationsState()

    class Success(
        locations: List<LocationsItem>
    ) : LocationsState(locations = locations)

    class Error(
        errorMessage: String
    ) : LocationsState(errorMessage = errorMessage)
}
