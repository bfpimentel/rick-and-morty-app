package dev.pimentel.rickandmorty.presentation.episodes.mappers

import dev.pimentel.domain.entities.Episode
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem

interface EpisodesItemMapper {
    fun get(episode: Episode): EpisodesItem
}

class EpisodesItemMapperImpl : EpisodesItemMapper {

    override fun get(episode: Episode): EpisodesItem =
        EpisodesItem(
            episode.id,
            episode.name,
            episode.airDate,
            episode.number
        )
}
