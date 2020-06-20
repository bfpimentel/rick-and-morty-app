package dev.pimentel.domain.models

open class CharacterModel(
    val id: Int,
    val name: String,
    val status: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val episodes: List<String>,
    val image: String
) {

    open class Origin(
        val name: String,
        val url: String
    )

    open class Location(
        val name: String,
        val url: String
    )
}
