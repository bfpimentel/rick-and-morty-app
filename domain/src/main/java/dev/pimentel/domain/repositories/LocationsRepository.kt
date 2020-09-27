package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.LocationModel
import dev.pimentel.domain.models.PagedResponseModel

interface LocationsRepository {
    suspend fun getLocations(
        page: Int,
        name: String?,
        type: String?,
        dimension: String?
    ): PagedResponseModel<LocationModel>
}
