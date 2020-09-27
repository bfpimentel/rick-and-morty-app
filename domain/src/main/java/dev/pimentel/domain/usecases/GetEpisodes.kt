package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Episode
import dev.pimentel.domain.entities.Pageable
import dev.pimentel.domain.repositories.EpisodesRepository
import dev.pimentel.domain.usecases.shared.UseCase

class GetEpisodes(
    private val episodesRepository: EpisodesRepository
) : UseCase<GetEpisodes.Params, Pageable<Episode>> {

    override suspend fun invoke(params: Params): Pageable<Episode> =
        episodesRepository.getEpisodes(
            params.page,
            params.name,
            params.number
        ).let { pagedResponse ->
            pagedResponse.results.map { episodeModel ->
                Episode(
                    episodeModel.id,
                    episodeModel.name,
                    episodeModel.airDate,
                    episodeModel.number
                )
            }.let { episodes ->
                Pageable(
                    pagedResponse.pages,
                    episodes
                )
            }
        }

    data class Params(
        val page: Int,
        val name: String? = null,
        val number: String? = null
    )
}
