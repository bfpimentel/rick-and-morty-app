package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.FilterModel
import io.reactivex.rxjava3.core.Single

interface CharacterNamesRepository {

    fun getAllCharacterFilters(): Single<List<FilterModel>>
}
