package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.CharacterDetailsModel
import dev.pimentel.domain.models.CharacterModel
import dev.pimentel.domain.models.PagedResponseModel

interface CharactersRepository {
    suspend fun getCharacters(
        page: Int,
        name: String?,
        species: String?,
        status: String?,
        gender: String?
    ): PagedResponseModel<CharacterModel>

    suspend fun getCharacterDetails(id: Int): CharacterDetailsModel
}
