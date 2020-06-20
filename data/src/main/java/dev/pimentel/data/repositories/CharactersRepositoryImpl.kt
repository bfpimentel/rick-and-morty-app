package dev.pimentel.data.repositories

import dev.pimentel.data.sources.CharactersDataSource
import dev.pimentel.domain.models.CharacterModel
import dev.pimentel.domain.models.PagedResponse
import dev.pimentel.domain.repositories.CharactersRepository
import io.reactivex.rxjava3.core.Single

class CharactersRepositoryImpl(
    private val charactersDataSource: CharactersDataSource
) : CharactersRepository {

    override fun getCharacters(page: Int): Single<PagedResponse<CharacterModel>> =
        charactersDataSource.getCharacters(page)
}
