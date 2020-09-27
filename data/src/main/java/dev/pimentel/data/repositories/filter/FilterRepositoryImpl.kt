package dev.pimentel.data.repositories.filter

import dev.pimentel.data.dto.FilterDTO
import dev.pimentel.data.sources.local.FilterLocalDataSource
import dev.pimentel.domain.models.FilterModel
import dev.pimentel.domain.repositories.FilterRepository

class FilterRepositoryImpl(
    private val localDataSource: FilterLocalDataSource,
    private val filterTypeModelMapper: FilterTypeModelMapper
) : FilterRepository {

    override suspend fun saveFilter(value: String, type: FilterModel.Type) =
        localDataSource.saveFilter(
            FilterDTO(
                value,
                filterTypeModelMapper.mapToData(type)
            )
        )

    override suspend fun getFiltersByType(type: FilterModel.Type): List<String> =
        localDataSource.getFilters(filterTypeModelMapper.mapToData(type))
            .map(FilterDTO::value)
}
