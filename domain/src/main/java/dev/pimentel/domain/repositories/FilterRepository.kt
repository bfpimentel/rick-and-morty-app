package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.FilterModel
import io.reactivex.rxjava3.core.Single

interface FilterRepository {
    fun saveFilter(filter: FilterModel)
    fun getFiltersByType(type: FilterModel.Type): Single<List<String>>
}
