package dev.pimentel.data.repositories.episodes

import dev.pimentel.data.models.EpisodeModelImpl
import dev.pimentel.data.models.PagedResponseModelImpl
import dev.pimentel.data.sources.remote.EpisodesRemoteDataSource
import dev.pimentel.domain.models.EpisodeModel
import dev.pimentel.domain.models.PagedResponseModel
import dev.pimentel.domain.repositories.EpisodesRepository

class EpisodesRepositoryImpl(
    private val remoteDataSource: EpisodesRemoteDataSource
) : EpisodesRepository {

    override suspend fun getEpisodes(
        page: Int,
        name: String?,
        number: String?
    ): PagedResponseModel<EpisodeModel> =
        remoteDataSource.getEpisodes(
            page,
            name,
            number
        ).let { response ->
            PagedResponseModelImpl(
                response.info.pages,
                response.results.map { episodeResponse ->
                    EpisodeModelImpl(
                        episodeResponse.id,
                        episodeResponse.name,
                        episodeResponse.airDate,
                        episodeResponse.number,
                    )
                }
            )
        }
}
