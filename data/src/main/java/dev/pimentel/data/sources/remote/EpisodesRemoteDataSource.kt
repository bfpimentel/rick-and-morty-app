package dev.pimentel.data.sources.remote

import dev.pimentel.data.dto.EpisodeDTO
import dev.pimentel.data.dto.PagedResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface EpisodesRemoteDataSource {

    @GET("episode/")
    suspend fun getEpisodes(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") number: String?
    ): PagedResponseDTO<EpisodeDTO>

    @GET
    suspend fun getEpisode(
        @Url url: String
    ): EpisodeDTO
}
