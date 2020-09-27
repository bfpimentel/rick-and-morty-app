package dev.pimentel.data.models

import dev.pimentel.domain.models.EpisodeModel

data class EpisodeModelImpl(
    override val id: Int,
    override val name: String,
    override val airDate: String,
    override val number: String
) : EpisodeModel
