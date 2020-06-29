package dev.pimentel.data.models

import com.squareup.moshi.Json
import dev.pimentel.domain.models.CharacterModel as DomainCharacterModel

data class CharacterModel(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "image") val image: String
) {

    fun toDomain() = DomainCharacterModel(
        id,
        name,
        status,
        image
    )
}
