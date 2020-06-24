package dev.pimentel.rickandmorty.presentation.characters.dto

sealed class CharactersState(
    val list: List<CharactersItem> = emptyList()
) {

    class Success(
        list: List<CharactersItem>
    ) : CharactersState(
        list = list
    )

    class Empty: CharactersState()
}
