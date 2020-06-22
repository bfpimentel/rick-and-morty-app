package dev.pimentel.data.models

import com.squareup.moshi.Json
import dev.pimentel.domain.models.CharacterModel as DomainCharacterModel

data class CharacterModel(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "type") val type: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "origin") val origin: Origin,
    @Json(name = "location") val location: Location,
    @Json(name = "episode") val episodes: List<String>,
    @Json(name = "image") val image: String
) {

    fun toDomain() = DomainCharacterModel(
        id,
        name,
        status,
        type,
        gender,
        origin.toDomain(),
        location.toDomain(),
        episodes,
        image
    )

    data class Origin(
        val name: String,
        val url: String
    ) {

        fun toDomain() = DomainCharacterModel.Origin(
            name,
            url
        )
    }

    data class Location(
        val name: String,
        val url: String
    ) {

        fun toDomain() = DomainCharacterModel.Location(
            name,
            url
        )
    }
}
