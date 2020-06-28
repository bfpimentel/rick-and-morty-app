package dev.pimentel.domain.repositories

import dev.pimentel.domain.models.LocationModel
import dev.pimentel.domain.models.PagedResponse
import io.reactivex.rxjava3.core.Single

interface LocationsRepository {
    fun getLocations(
        page: Int,
        name: String?,
        type: String?,
        dimension: String?
    ): Single<PagedResponse<LocationModel>>
}
