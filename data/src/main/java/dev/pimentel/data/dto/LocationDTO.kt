package dev.pimentel.data.dto

import com.squareup.moshi.Json

data class LocationDTO(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "dimension") val dimension: String
)
