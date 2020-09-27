package dev.pimentel.data.models

import dev.pimentel.domain.models.CharacterDetailsModel

data class CharacterDetailsModelImpl(
    override val id: Int,
    override val name: String,
    override val status: String,
    override val species: String,
    override val type: String,
    override val gender: String,
    override val origin: String,
    override val location: String,
    override val image: String,
    override val episodes: List<EpisodeModelImpl>
) : CharacterDetailsModel
