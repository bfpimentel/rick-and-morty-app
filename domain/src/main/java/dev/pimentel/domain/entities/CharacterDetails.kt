package dev.pimentel.domain.entities

data class CharacterDetails(
    val id: Int,
    val name: String,
    val status: String,
    val image: String,
    val gender: String,
    val origin: String,
    val type: String,
    val location: String,
    val episodes: List<Episode>
)
