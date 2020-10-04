package dev.pimentel.rickandmorty.presentation.characters.filter.dto

data class CharactersFilterState(
    val canApplyFilter: Boolean = false,
    val canClear: Boolean = false,
    val name: String? = null,
    val species: String? = null,
    val selectedStatusIndex: Int? = null,
    val selectedGenderIndex: Int? = null,
    val result: CharactersFilter? = null
)
