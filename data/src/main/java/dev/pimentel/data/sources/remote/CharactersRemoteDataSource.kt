package dev.pimentel.data.sources.remote

import dev.pimentel.data.dto.CharacterDTO
import dev.pimentel.data.dto.CharacterDetailsDTO
import dev.pimentel.data.dto.PagedResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersRemoteDataSource {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("species") species: String?,
        @Query("status") status: String?,
        @Query("gender") gender: String?
    ): PagedResponseDTO<CharacterDTO>

    @GET("character/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: Int
    ): CharacterDetailsDTO
}
