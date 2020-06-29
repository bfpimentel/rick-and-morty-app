package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.CharacterDetailsModel
import dev.pimentel.domain.models.CharacterModel
import dev.pimentel.domain.models.PagedResponse
import io.reactivex.rxjava3.core.Single

interface CharactersRepository {
    fun getCharacters(
        page: Int,
        name: String?,
        species: String?,
        status: String?,
        gender: String?
    ): Single<PagedResponse<CharacterModel>>

    fun getCharacterDetails(id: Int): Single<CharacterDetailsModel>
}
