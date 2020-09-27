package dev.pimentel.rickandmorty.presentation.episodes.dto

import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter

sealed class EpisodesIntent {

    data class GetEpisodes(val filter: EpisodesFilter) : EpisodesIntent()

    object GetEpisodesWithLastFilter : EpisodesIntent()

    object OpenFilters : EpisodesIntent()
}
