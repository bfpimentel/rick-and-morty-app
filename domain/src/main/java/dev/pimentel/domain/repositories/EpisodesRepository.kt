package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.EpisodeModel
import dev.pimentel.domain.models.PagedResponse
import io.reactivex.rxjava3.core.Single

interface EpisodesRepository {
    fun getEpisodes(
        page: Int,
        name: String?,
        number: String?
    ): Single<PagedResponse<EpisodeModel>>
}
