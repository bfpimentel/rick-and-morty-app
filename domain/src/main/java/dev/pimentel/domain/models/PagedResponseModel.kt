package dev.pimentel.domain.models

interface PagedResponseModel<T> {
    val pages: Int
    val results: List<T>
}
