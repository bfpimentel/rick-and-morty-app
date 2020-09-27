package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.FilterModel

interface FilterRepository {
    suspend fun saveFilter(value: String, type: FilterModel.Type)
    suspend fun getFiltersByType(type: FilterModel.Type): List<String>
}
