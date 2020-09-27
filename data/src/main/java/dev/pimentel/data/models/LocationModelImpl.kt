package dev.pimentel.data.models

import dev.pimentel.domain.models.LocationModel

data class LocationModelImpl(
    override val id: Int,
    override val name: String,
    override val type: String,
    override val dimension: String
) : LocationModel
