package dev.pimentel.data.repositories.locations

import dev.pimentel.data.models.LocationModel
import dev.pimentel.data.sources.remote.LocationsRemoteDataSource
import dev.pimentel.domain.repositories.LocationsRepository
import io.reactivex.rxjava3.core.Single
import dev.pimentel.domain.models.LocationModel as DomainLocationModel
import dev.pimentel.domain.models.PagedResponse as DomainPagedResponse

class LocationsRepositoryImpl(
    private val remoteDataSource: LocationsRemoteDataSource
) : LocationsRepository {

    override fun getLocations(
        page: Int,
        name: String?,
        type: String?,
        dimension: String?
    ): Single<DomainPagedResponse<DomainLocationModel>> =
        remoteDataSource.getLocations(
            page,
            name,
            type,
            dimension
        ).map { response ->
            DomainPagedResponse(
                response.info.pages,
                response.results.map(LocationModel::toDomain)
            )
        }
}
