package dev.pimentel.domain.usecases

import dev.pimentel.domain.repositories.CharacterNamesRepository
import dev.pimentel.domain.usecases.shared.NoParams
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.rxjava3.core.Completable

class TestGet(
    private val characterNamesRepository: CharacterNamesRepository
) : UseCase<NoParams, Completable> {

    override fun invoke(params: NoParams): Completable =
        Completable.fromSingle(characterNamesRepository.getAllCharacterFilters())
}
