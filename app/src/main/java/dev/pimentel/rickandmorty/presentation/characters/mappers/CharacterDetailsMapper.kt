package dev.pimentel.rickandmorty.presentation.characters.mappers

import android.content.Context
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.characters.details.dto.CharacterDetails
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem
import dev.pimentel.domain.entities.CharacterDetails as CharacterDetailsEntity

interface CharacterDetailsMapper {
    fun get(details: CharacterDetailsEntity): CharacterDetails
}

class CharacterDetailsMapperImpl(
    private val context: Context
) : CharacterDetailsMapper {

    override fun get(details: CharacterDetailsEntity): CharacterDetails =
        CharacterDetails(
            details.id,
            details.name,
            details.status,
            details.species,
            details.image,
            details.gender,
            details.origin,
            if (details.type.isEmpty()) context.getString(R.string.characters_details_unknown)
            else details.type,
            details.location,
            details.episodes.map { episode ->
                EpisodesItem(
                    episode.id,
                    episode.name,
                    episode.airDate,
                    episode.number
                )
            }
        )
}
