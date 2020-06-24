package dev.pimentel.rickandmorty.presentation.characters.mappers

import dev.pimentel.domain.entities.Character
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersItem

interface CharacterDisplayMapper {
    fun get(character: Character): CharactersItem
}

class CharacterDisplayMapperImpl : CharacterDisplayMapper {

    override fun get(character: Character): CharactersItem =
        CharactersItem(
            character.id,
            character.image,
            character.status.capitalize(),
            character.name
        )
}
