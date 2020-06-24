package dev.pimentel.rickandmorty.presentation.characters.filter.dto

data class CharactersFilterState(
    val canApplyFilter: Boolean = false,
    val canClear: Boolean = false,
    val name: String? = null,
    val statusId: Int? = null,
    val genderId: Int? = null
)
