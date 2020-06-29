package dev.pimentel.data.models

import com.squareup.moshi.Json
import dev.pimentel.domain.models.EpisodeModel as DomainEpisodeModel

data class EpisodeModel(
    val id: Int,
    val name: String,
    @Json(name = "air_date") val airDate: String,
    @Json(name = "episode") val number: String
) {

    fun toDomain() = DomainEpisodeModel(
        id,
        name,
        airDate,
        number
    )
}
