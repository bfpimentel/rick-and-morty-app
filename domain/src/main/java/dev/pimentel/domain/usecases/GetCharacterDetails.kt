package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.CharacterDetails
import dev.pimentel.domain.entities.Episode
import dev.pimentel.domain.repositories.CharactersRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.rxjava3.core.Single

class GetCharacterDetails(
    private val charactersRepository: CharactersRepository
) : UseCase<GetCharacterDetails.Params, Single<CharacterDetails>> {

    override fun invoke(params: Params): Single<CharacterDetails> =
        charactersRepository.getCharacterDetails(params.id).map { detailsModel ->
            CharacterDetails(
                detailsModel.id,
                detailsModel.name,
                detailsModel.status,
                detailsModel.species,
                detailsModel.image,
                detailsModel.gender,
                detailsModel.origin,
                detailsModel.type,
                detailsModel.location,
                detailsModel.episodes.map { episodeModel ->
                    Episode(
                        episodeModel.id,
                        episodeModel.name,
                        episodeModel.airDate,
                        episodeModel.number
                    )
                }
            )
        }

    data class Params(
        val id: Int
    )
}
