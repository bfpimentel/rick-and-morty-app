package dev.pimentel.rickandmorty.presentation.locations.filter.dto

data class LocationsFilterState(
    val canApplyFilter: Boolean = false,
    val canClear: Boolean = false,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null
)
