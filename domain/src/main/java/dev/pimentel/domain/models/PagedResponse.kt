package dev.pimentel.domain.models

data class PagedResponse<T>(
    val pages: Int,
    val results: List<T>
)
