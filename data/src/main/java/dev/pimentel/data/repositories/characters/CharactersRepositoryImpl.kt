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
            page = page,
            name = name,
            species = species,
            status = status,
            gender = gender
        ).let { response ->
            PagedResponseModelImpl(
                response.info.pages,
                response.results.map { characterResponse ->
                    CharacterModelImpl(
                        id = characterResponse.id,
                        name = characterResponse.name,
                        status = characterResponse.status,
                        image = characterResponse.image
                    )
                }
            )
        }

    override suspend fun getCharacterDetails(id: Int): CharacterDetailsModel =
        charactersRemoteDataSource.getCharacterDetails(id)
            .let { characterDetails ->
                characterDetails.episodes.map { episodeId ->
                    episodesRemoteDataSource.getEpisode(episodeId).let { episodeResponse ->
                        EpisodeModelImpl(
                            id = episodeResponse.id,
                            name = episodeResponse.name,
                            airDate = episodeResponse.airDate,
                            number = episodeResponse.number
                        )
                    }
                }.let { episodes ->
                    CharacterDetailsModelImpl(
                        id = characterDetails.id,
                        name = characterDetails.name,
                        status = characterDetails.status,
                        species = characterDetails.species,
                        type = characterDetails.type,
                        gender = characterDetails.gender,
                        origin = characterDetails.origin.name,
                        location = characterDetails.location.name,
                        image = characterDetails.image,
                        episodes = episodes
                    )
                }
            }
}
