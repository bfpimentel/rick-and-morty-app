package dev.pimentel.data.sources

import dev.pimentel.domain.models.CharacterModel
import dev.pimentel.domain.models.PagedResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersDataSource {

    @GET("character/")
    fun getCharacters(
        @Query("page") page: Int
    ): Single<PagedResponse<CharacterModel>>
}
