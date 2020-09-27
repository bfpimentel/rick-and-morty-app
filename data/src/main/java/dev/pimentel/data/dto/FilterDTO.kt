package dev.pimentel.data.dto

data class FilterDTO(
    val value: String,
    val type: Type
) {

    enum class Type {
        CHARACTER_NAME, CHARACTER_SPECIES,
        LOCATION_NAME, LOCATION_TYPE, LOCATION_DIMENSION,
        EPISODE_NAME, EPISODE_NUMBER
    }
}
