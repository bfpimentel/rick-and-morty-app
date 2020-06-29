package dev.pimentel.rickandmorty.presentation.characters.mappers

import dev.pimentel.domain.entities.Character
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersItem

interface CharactersItemsMapper {
    fun getAll(characters: List<Character>): List<CharactersItem>
}

class CharactersItemsMapperImpl : CharactersItemsMapper {

    override fun getAll(characters: List<Character>): List<CharactersItem> =
        characters.map { character ->
            CharactersItem(
                character.id,
                character.image,
                character.status.capitalize(),
                character.name
            )
        }
}
