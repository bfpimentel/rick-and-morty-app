package dev.pimentel.data.models

import dev.pimentel.domain.models.FilterModel as DomainFilterModel

data class FilterModel(
    val value: String,
    val type: Type
) {

    fun toDomain() = DomainFilterModel(value)

    enum class Type {
        CHARACTER_NAME, CHARACTER_SPECIES,
        LOCATION_NAME, LOCATION_TYPE, LOCATION_DIMENSION,
        EPISODE_NAME, EPISODE_NUMBER
    }
}
