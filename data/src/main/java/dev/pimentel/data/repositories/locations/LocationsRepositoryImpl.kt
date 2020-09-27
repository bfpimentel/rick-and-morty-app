package dev.pimentel.data.repositories.locations

import dev.pimentel.data.models.LocationModelImpl
import dev.pimentel.data.models.PagedResponseModelImpl
import dev.pimentel.data.sources.remote.LocationsRemoteDataSource
import dev.pimentel.domain.models.LocationModel
import dev.pimentel.domain.models.PagedResponseModel
import dev.pimentel.domain.repositories.LocationsRepository

class LocationsRepositoryImpl(
    private val remoteDataSource: LocationsRemoteDataSource
) : LocationsRepository {

    override suspend fun getLocations(
        page: Int,
        name: String?,
        type: String?,
        dimension: String?
    ): PagedResponseModel<LocationModel> =
        remoteDataSource.getLocations(
            page,
            name,
            type,
            dimension
        ).let { response ->
            PagedResponseModelImpl(
                response.info.pages,
                response.results.map { locationResponse ->
                    LocationModelImpl(
                        locationResponse.id,
                        locationResponse.name,
                        locationResponse.type,
                        locationResponse.dimension
                    )
                }
            )
        }
}
