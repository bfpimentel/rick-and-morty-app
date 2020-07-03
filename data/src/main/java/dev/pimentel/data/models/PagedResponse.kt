package dev.pimentel.data.models

import com.squareup.moshi.Json

data class PagedResponse<T>(
    @Json(name = "info") val info: Info,
    @Json(name = "results") val results: List<T>
) {

    data class Info(
        @Json(name = "pages") val pages: Int
    )
}
