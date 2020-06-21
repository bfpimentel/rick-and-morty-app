package dev.pimentel.rickandmorty.presentation.characters.data

sealed class CharactersState(
    val list: List<CharacterDisplay> = emptyList()
) {

    class Success(
        list: List<CharacterDisplay>
    ) : CharactersState(
        list = list
    )
}
