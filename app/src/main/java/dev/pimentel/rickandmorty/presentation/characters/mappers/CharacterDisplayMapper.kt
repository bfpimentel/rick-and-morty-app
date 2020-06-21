package dev.pimentel.rickandmorty.presentation.characters.mappers

import dev.pimentel.domain.entities.Character
import dev.pimentel.rickandmorty.presentation.characters.data.CharacterDisplay

interface CharacterDisplayMapper {
    fun get(character: Character): CharacterDisplay
}

class CharacterDisplayMapperImpl : CharacterDisplayMapper {

    override fun get(character: Character): CharacterDisplay =
        CharacterDisplay(
            character.id,
            character.image,
            character.status.capitalize(),
            character.name
        )
}
