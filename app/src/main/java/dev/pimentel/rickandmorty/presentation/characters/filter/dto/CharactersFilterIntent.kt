package dev.pimentel.rickandmorty.presentation.characters.filter.dto

import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult

sealed class CharactersFilterIntent {

    data class Initialize(val filter: CharactersFilter) : CharactersFilterIntent()

    object OpenNameFilter : CharactersFilterIntent()

    object OpenSpeciesFilter : CharactersFilterIntent()

    data class SetTextFilter(val filterResult: FilterResult) : CharactersFilterIntent()

    data class SetStatus(val selectedStatusIndex: Int) : CharactersFilterIntent()

    data class SetGender(val selectedGenderIndex: Int) : CharactersFilterIntent()

    object ClearFilter : CharactersFilterIntent()

    object ApplyFilter : CharactersFilterIntent()
}
