package dev.pimentel.data.dto

import com.squareup.moshi.Json

data class EpisodeDTO(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "air_date") val airDate: String,
    @Json(name = "episode") val number: String
)
