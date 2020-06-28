package dev.pimentel.rickandmorty.presentation.episodes.mappers

import android.content.Context
import dev.pimentel.domain.entities.Episode
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem

interface EpisodesItemMapper {
    fun getAll(episodes: List<Episode>): List<EpisodesItem>
}

class EpisodesItemMapperImpl(
    val context: Context
) : EpisodesItemMapper {

    override fun getAll(episodes: List<Episode>): List<EpisodesItem> {
        val itemList = mutableListOf<EpisodesItem>()
        episodes.groupBy { episode -> episode.number.substring(0..2) }
            .forEach { group ->
                itemList.add(EpisodesItem.group(group.key.substring(1..2)))
                itemList.addAll(
                    group.value.map { episode ->
                        EpisodesItem(
                            episode.id,
                            episode.name,
                            episode.airDate,
                            episode.number
                        )
                    }
                )
            }
        return itemList
    }
}
