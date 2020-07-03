package dev.pimentel.rickandmorty.presentation.locations.dto

sealed class LocationsState(
    val locations: List<LocationsItem> = emptyList(),
    val scrollToTheTop: Unit? = null,
    val errorMessage: String? = null
) {

    class Empty : LocationsState(scrollToTheTop = Unit)

    class Success(
        locations: List<LocationsItem>
    ) : LocationsState(locations = locations)

    class Error(
        errorMessage: String
    ) : LocationsState(errorMessage = errorMessage)
}
