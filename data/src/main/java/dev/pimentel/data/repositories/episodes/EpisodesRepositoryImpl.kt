package dev.pimentel.data.repositories.episodes

import dev.pimentel.data.models.EpisodeModel
import dev.pimentel.data.sources.remote.EpisodesRemoteDataSource
import dev.pimentel.domain.repositories.EpisodesRepository
import io.reactivex.rxjava3.core.Single
import dev.pimentel.domain.models.EpisodeModel as DomainEpisodeModel
import dev.pimentel.domain.models.PagedResponse as DomainPagedResponse

class EpisodesRepositoryImpl(
    private val remoteDataSource: EpisodesRemoteDataSource
) : EpisodesRepository {

    override fun getEpisodes(
        page: Int,
        name: String?,
        number: String?
    ): Single<DomainPagedResponse<DomainEpisodeModel>> =
        remoteDataSource.getEpisodes(
            page,
            name,
            number
        ).map { response ->
            DomainPagedResponse(
                response.info.pages,
                response.results.map(EpisodeModel::toDomain)
            )
        }
}
