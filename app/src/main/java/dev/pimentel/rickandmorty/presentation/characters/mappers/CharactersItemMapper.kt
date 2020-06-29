package dev.pimentel.rickandmorty.presentation.characters.mappers

import dev.pimentel.domain.entities.Character
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersItem

interface CharactersItemMapper {
    fun get(character: Character): CharactersItem
}

class CharactersItemMapperImpl : CharactersItemMapper {

    override fun get(character: Character): CharactersItem =
        CharactersItem(
            character.id,
            character.image,
            character.status.capitalize(),
            character.name
        )
}
