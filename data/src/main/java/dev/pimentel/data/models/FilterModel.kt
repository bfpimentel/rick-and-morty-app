package dev.pimentel.data.models

data class FilterModel(
    val value: String,
    val type: Type
) {

    enum class Type {
        CHARACTER_NAME, CHARACTER_SPECIES,
        LOCATION_NAME, LOCATION_TYPE, LOCATION_DIMENSION,
        EPISODE_NAME, EPISODE_NUMBER
    }
}
