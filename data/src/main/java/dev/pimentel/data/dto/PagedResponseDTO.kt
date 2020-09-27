package dev.pimentel.data.dto

import com.squareup.moshi.Json

data class PagedResponseDTO<T>(
    @Json(name = "info") val info: Info,
    @Json(name = "results") val results: List<T>
) {

    data class Info(
        @Json(name = "pages") val pages: Int
    )
}
