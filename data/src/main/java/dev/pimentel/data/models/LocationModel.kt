package dev.pimentel.data.models

import dev.pimentel.domain.models.LocationModel as DomainLocationModel

data class LocationModel(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
) {

    fun toDomain() = DomainLocationModel(
        id,
        name,
        type,
        dimension
    )
}
