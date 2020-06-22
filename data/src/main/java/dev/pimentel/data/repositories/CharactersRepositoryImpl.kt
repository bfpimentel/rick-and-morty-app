package dev.pimentel.data.repositories

import dev.pimentel.data.models.CharacterModel
import dev.pimentel.data.sources.CharactersDataSource
import dev.pimentel.domain.repositories.CharactersRepository
import io.reactivex.rxjava3.core.Single
import dev.pimentel.domain.models.CharacterModel as DomainCharacterModel
import dev.pimentel.domain.models.PagedResponse as DomainPagedResponse

class CharactersRepositoryImpl(
    private val charactersDataSource: CharactersDataSource
) : CharactersRepository {

    override fun getCharacters(page: Int): Single<DomainPagedResponse<DomainCharacterModel>> =
        charactersDataSource.getCharacters(page).map { response ->
            DomainPagedResponse(
                response.info.pages,
                response.results.map(CharacterModel::toDomain)
            )
        }
}
