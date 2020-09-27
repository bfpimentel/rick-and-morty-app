package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Character
import dev.pimentel.domain.entities.Pageable
import dev.pimentel.domain.repositories.CharactersRepository
import dev.pimentel.domain.usecases.shared.UseCase

class GetCharacters(
    private val charactersRepository: CharactersRepository
) : UseCase<GetCharacters.Params, Pageable<Character>> {

    override suspend fun invoke(params: Params): Pageable<Character> =
        charactersRepository.getCharacters(
            params.page,
            params.name,
            params.species,
            params.status,
            params.gender
        ).let { pagedResponse ->
            pagedResponse.results.map { characterModel ->
                Character(
                    characterModel.id,
                    characterModel.name,
                    characterModel.status,
                    characterModel.image
                )
            }.let { characters ->
                Pageable(
                    pagedResponse.pages,
                    characters
                )
            }
        }

    data class Params(
        val page: Int,
        val name: String? = null,
        val species: String? = null,
        val status: String? = null,
        val gender: String? = null
    )
}
