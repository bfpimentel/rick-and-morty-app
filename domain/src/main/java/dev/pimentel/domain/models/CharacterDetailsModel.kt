package dev.pimentel.domain.models

data class CharacterDetailsModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val origin: String,
    val location: String,
    val episodes: List<EpisodeModel>
)
