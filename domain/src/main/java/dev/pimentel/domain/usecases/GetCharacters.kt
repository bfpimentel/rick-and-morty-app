package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Character
import dev.pimentel.domain.repositories.CharactersRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.rxjava3.core.Single

class GetCharacters(
    private val charactersRepository: CharactersRepository
) : UseCase<NoParams, Single<List<Character>>> {

    override fun invoke(params: NoParams): Single<List<Character>> =
        charactersRepository.getCharacters(1).map { pagedResponse ->
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
            }
        }
}
