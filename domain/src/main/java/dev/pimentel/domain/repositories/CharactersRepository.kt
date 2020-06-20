package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.CharacterModel
import dev.pimentel.domain.models.PagedResponse
import io.reactivex.rxjava3.core.Single

interface CharactersRepository {
    fun getCharacters(page: Int): Single<PagedResponse<CharacterModel>>
}
