package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Character
import dev.pimentel.domain.repositories.CharactersRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.rxjava3.core.Single

class GetCharacters(
    private val charactersRepository: CharactersRepository
) : UseCase<GetCharacters.Params, Single<GetCharacters.Response>> {

    override fun invoke(params: Params): Single<Response> =
        charactersRepository.getCharacters(
            params.page,
            params.name,
            params.species,
            params.status,
            params.gender
        ).map { pagedResponse ->
            pagedResponse.results.map { characterModel ->
                Character(
                    characterModel.id,
                    characterModel.name,
                    characterModel.status,
                    characterModel.type,
                    characterModel.gender,
                    characterModel.origin.let { originModel ->
                        Character.Origin(
                            originModel.name,
                            originModel.url
                        )
                    },
                    characterModel.location.let { locationModel ->
                        Character.Location(
                            locationModel.name,
                            locationModel.url
                        )
                    },
                    characterModel.episodes,
                    characterModel.image
                )
            }.let { characters ->
                Response(
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

    data class Response(
        val pages: Int,
        val characters: List<Character>
    )
}
