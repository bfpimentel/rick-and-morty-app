package dev.pimentel.data.repositories.characters

import dev.pimentel.data.models.CharacterModel
import dev.pimentel.data.sources.remote.CharactersRemoteDataSource
import dev.pimentel.domain.repositories.CharactersRepository
import io.reactivex.rxjava3.core.Single
import dev.pimentel.domain.models.CharacterModel as DomainCharacterModel
import dev.pimentel.domain.models.PagedResponse as DomainPagedResponse

class CharactersRepositoryImpl(
    private val charactersDataSource: CharactersRemoteDataSource
) : CharactersRepository {

    override fun getCharacters(
        page: Int,
        name: String?,
        species: String?,
        status: String?,
        gender: String?
    ): Single<DomainPagedResponse<DomainCharacterModel>> =
        charactersDataSource.getCharacters(
            page,
            name,
            species,
            status,
            gender
        ).map { response ->
            DomainPagedResponse(
                response.info.pages,
                response.results.map(CharacterModel::toDomain)
            )
        }
}
