package dev.pimentel.data.repositories

import dev.pimentel.data.models.FilterModel
import dev.pimentel.data.sources.local.FilterLocalDataSource
import dev.pimentel.domain.repositories.CharacterNamesRepository
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Single
import dev.pimentel.domain.models.FilterModel as DomainCharacterNameModel

class CharacterNamesRepositoryImpl(
    private val localDataSource: FilterLocalDataSource
) : CharacterNamesRepository {

    override fun getAllNames(): Single<List<DomainCharacterNameModel>> =
        localDataSource.getFilters().map { response ->
            response.map(FilterModel::toDomain)
        }.`as`(RxJavaBridge.toV3Single())
}
