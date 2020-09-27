package dev.pimentel.data.dto

import com.squareup.moshi.Json

data class CharacterDTO(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "image") val image: String
)
