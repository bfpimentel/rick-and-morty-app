package dev.pimentel.rickandmorty.presentation.episodes.filter.dto

data class EpisodesFilterState(
    val canApplyFilter: Boolean = false,
    val canClear: Boolean = false,
    val name: String? = null,
    val number: String? = null
)
