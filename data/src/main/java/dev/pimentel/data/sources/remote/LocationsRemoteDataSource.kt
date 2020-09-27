package dev.pimentel.data.sources.remote

import dev.pimentel.data.dto.LocationDTO
import dev.pimentel.data.dto.PagedResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface LocationsRemoteDataSource {

    @GET("location/")
    suspend fun getLocations(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?
    ): PagedResponseDTO<LocationDTO>

    @GET
    suspend fun getLocation(
        @Url url: String
    ): LocationDTO
}
