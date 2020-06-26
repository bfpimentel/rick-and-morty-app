package dev.pimentel.data.repositories

import dev.pimentel.data.models.FilterModel
import dev.pimentel.data.sources.local.FilterLocalDataSource
import dev.pimentel.domain.repositories.CharacterNamesRepository
import io.reactivex.rxjava3.core.Single
import dev.pimentel.domain.models.FilterModel as DomainCharacterNameModel

class CharacterNamesRepositoryImpl(
    private val localDataSource: FilterLocalDataSource
) : CharacterNamesRepository {

    override fun getAllCharacterFilters(): Single<List<DomainCharacterNameModel>> =
        localDataSource.getFilters(FilterModel.Type.CHARACTER_NAME).map { response ->
            response.map(FilterModel::toDomain)
        }
}
