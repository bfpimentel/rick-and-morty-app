package dev.pimentel.domain.entities

data class Character(
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

    data class Origin(
        val name: String,
        val url: String
    )

    data class Location(
        val name: String,
        val url: String
    )
}
