package dev.pimentel.rickandmorty.presentation.characters.dto

import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter

sealed class CharactersIntent {

    data class GetCharacters(val filter: CharactersFilter) : CharactersIntent()

    object GetCharactersWithLastFilter : CharactersIntent()

    object OpenFilters : CharactersIntent()

    data class GetDetails(val characterId: Int) : CharactersIntent()
}
