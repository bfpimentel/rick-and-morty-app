package dev.pimentel.domain.entities

data class Pageable<T>(
    val lastPage: Int,
    val items: List<T>
)
