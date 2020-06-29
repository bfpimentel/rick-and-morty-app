package dev.pimentel.rickandmorty.presentation.characters.dto

sealed class CharactersState(
    val characters: List<CharactersItem> = emptyList(),
    val scrollToTheTop: Unit? = null,
    val errorMessage: String? = null
) {

    class Empty : CharactersState(
        scrollToTheTop = Unit
    )

    class Success(
        characters: List<CharactersItem>
    ) : CharactersState(characters = characters)

    class Error(
        errorMessage: String
    ) : CharactersState(errorMessage = errorMessage)
}
