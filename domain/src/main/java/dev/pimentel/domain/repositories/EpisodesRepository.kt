package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.EpisodeModel
import dev.pimentel.domain.models.PagedResponseModel

interface EpisodesRepository {
    suspend fun getEpisodes(
        page: Int,
        name: String?,
        number: String?
    ): PagedResponseModel<EpisodeModel>
}
