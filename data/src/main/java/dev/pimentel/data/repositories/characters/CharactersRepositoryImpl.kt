package dev.pimentel.data.repositories.characters

import dev.pimentel.data.models.CharacterModel
import dev.pimentel.data.models.EpisodeModel
import dev.pimentel.data.sources.remote.CharactersRemoteDataSource
import dev.pimentel.data.sources.remote.EpisodesRemoteDataSource
import dev.pimentel.domain.repositories.CharactersRepository
import io.reactivex.rxjava3.core.Single
import dev.pimentel.domain.models.CharacterDetailsModel as DomainCharacterDetailsModel
import dev.pimentel.domain.models.CharacterModel as DomainCharacterModel
import dev.pimentel.domain.models.PagedResponse as DomainPagedResponse

class CharactersRepositoryImpl(
    private val charactersRemoteDataSource: CharactersRemoteDataSource,
    private val episodesRemoteDataSource: EpisodesRemoteDataSource
) : CharactersRepository {

    override fun getCharacters(
        page: Int,
        name: String?,
        species: String?,
        status: String?,
        gender: String?
    ): Single<DomainPagedResponse<DomainCharacterModel>> =
        charactersRemoteDataSource.getCharacters(
            page,
            name,
            species,
            status,
            gender
        ).map { response ->
            DomainPagedResponse(
                response.info.pages,
                response.results.map(CharacterModel::toDomain)
            )
        }

    override fun getCharacterDetails(id: Int): Single<DomainCharacterDetailsModel> =
        charactersRemoteDataSource.getCharacterDetails(id)
            .flatMap { characterDetails ->
                characterDetails.episodes
                    .map(episodesRemoteDataSource::getEpisode)
                    .let {
                        Single.zip(it) { episodes ->
                            @Suppress("UNCHECKED_CAST")
                            episodes.asList() as List<EpisodeModel>
                        }
                    }
                    .map { episodes ->
                        DomainCharacterDetailsModel(
                            characterDetails.id,
                            characterDetails.name,
                            characterDetails.status,
                            characterDetails.species,
                            characterDetails.type,
                            characterDetails.gender,
                            characterDetails.image,
                            characterDetails.origin.name,
                            characterDetails.location.name,
                            episodes.map(EpisodeModel::toDomain)
                        )
                    }
            }
}
