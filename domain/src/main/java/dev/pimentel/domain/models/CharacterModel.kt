package dev.pimentel.domain.models

data class CharacterModel(
    val id: Int,
    val name: String,
    val status: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val episode: List<String>,
    val image: String
) {

    data class Origin(
        val name: String,
        val url: String
    )

    data class Location(
        val name: String,
        val url: String
    )
}
