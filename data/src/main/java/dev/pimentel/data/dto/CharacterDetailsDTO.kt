package dev.pimentel.data.dto

import com.squareup.moshi.Json

data class CharacterDetailsDTO(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "species") val species: String,
    @Json(name = "type") val type: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "origin") val origin: OriginDTO,
    @Json(name = "location") val location: LocationDTO,
    @Json(name = "episode") val episodes: List<String>,
    @Json(name = "image") val image: String
) {

    data class OriginDTO(
        val name: String,
        val url: String
    )

    data class LocationDTO(
        val name: String,
        val url: String
    )
}
