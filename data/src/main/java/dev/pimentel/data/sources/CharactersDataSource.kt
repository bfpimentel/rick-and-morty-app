package dev.pimentel.data.sources

import dev.pimentel.data.models.CharacterModel
import dev.pimentel.data.models.PagedResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersDataSource {

    @GET("character/")
    fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("species") species: String?,
        @Query("status") status: String?,
        @Query("gender") gender: String?
    ): Single<PagedResponse<CharacterModel>>
}
