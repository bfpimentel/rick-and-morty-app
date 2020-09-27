package dev.pimentel.data.repositories.characters

import dev.pimentel.data.models.CharacterDetailsModelImpl
import dev.pimentel.data.models.CharacterModelImpl
import dev.pimentel.data.models.EpisodeModelImpl
import dev.pimentel.data.models.PagedResponseModelImpl
import dev.pimentel.data.sources.remote.CharactersRemoteDataSource
import dev.pimentel.data.sources.remote.EpisodesRemoteDataSource
import dev.pimentel.domain.models.CharacterDetailsModel
import dev.pimentel.domain.models.CharacterModel
import dev.pimentel.domain.models.PagedResponseModel
import dev.pimentel.domain.repositories.CharactersRepository

class CharactersRepositoryImpl(
    private val charactersRemoteDataSource: CharactersRemoteDataSource,
    private val episodesRemoteDataSource: EpisodesRemoteDataSource
) : CharactersRepository {

    override suspend fun getCharacters(
        page: Int,
        name: String?,
        species: String?,
        status: String?,
        gender: String?
    ): PagedResponseModel<CharacterModel> =
        charactersRemoteDataSource.getCharacters(
            page,
            name,
            species,
            status,
            gender
        ).let { response ->
            PagedResponseModelImpl(
                response.info.pages,
                response.results.map { characterResponse ->
                    CharacterModelImpl(
                        characterResponse.id,
                        characterResponse.name,
                        characterResponse.status,
                        characterResponse.image
                    )
                }
            )
        }

    override suspend fun getCharacterDetails(id: Int): CharacterDetailsModel =
        charactersRemoteDataSource.getCharacterDetails(id)
            .let { characterDetails ->
                characterDetails.episodes
                    .map { episodeId ->
                        episodesRemoteDataSource.getEpisode(episodeId).let { episodeResponse ->
                            EpisodeModelImpl(
                                episodeResponse.id,
                                episodeResponse.name,
                                episodeResponse.airDate,
                                episodeResponse.number
                            )
                        }
                    }.let { episodes ->
                        CharacterDetailsModelImpl(
                            characterDetails.id,
                            characterDetails.name,
                            characterDetails.status,
                            characterDetails.species,
                            characterDetails.type,
                            characterDetails.gender,
                            characterDetails.image,
                            characterDetails.origin.name,
                            characterDetails.location.name,
                            episodes
                        )
                    }
            }
}
