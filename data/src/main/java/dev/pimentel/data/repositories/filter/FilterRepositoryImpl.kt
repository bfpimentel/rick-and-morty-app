package dev.pimentel.data.repositories.filter

import dev.pimentel.data.models.FilterModel
import dev.pimentel.data.sources.local.FilterLocalDataSource
import dev.pimentel.domain.repositories.FilterRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import dev.pimentel.domain.models.FilterModel as DomainFilterModel

class FilterRepositoryImpl(
    private val localDataSource: FilterLocalDataSource,
    private val filterTypeModelMapper: FilterTypeModelMapper
) : FilterRepository {

    override fun saveFilter(filter: DomainFilterModel): Completable =
        localDataSource.saveFilter(
            FilterModel(
                filter.value,
                filterTypeModelMapper.mapToData(filter.type)
            )
        )

    override fun getFiltersByType(type: DomainFilterModel.Type): Single<List<String>> =
        filterTypeModelMapper.mapToData(type).let(localDataSource::getFilters).map { response ->
            response.map(FilterModel::value)
        }

}
