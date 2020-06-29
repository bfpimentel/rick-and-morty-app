package dev.pimentel.data.models

import com.squareup.moshi.Json

data class CharacterDetailsModel(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "species") val species: String,
    @Json(name = "type") val type: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "origin") val origin: Origin,
    @Json(name = "location") val location: Location,
    @Json(name = "episode") val episodes: List<String>,
    @Json(name = "image") val image: String
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
