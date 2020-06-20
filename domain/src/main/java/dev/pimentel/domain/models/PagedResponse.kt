package dev.pimentel.domain.models

class PagedResponse<T>(
    val pages: Int,
    val results: List<T>
)
