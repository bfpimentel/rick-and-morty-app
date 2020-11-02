package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Location
import dev.pimentel.domain.entities.Pageable
import dev.pimentel.domain.repositories.LocationsRepository
import dev.pimentel.domain.usecases.shared.UseCase

class GetLocations(
    private val locationsRepository: LocationsRepository
) : UseCase<GetLocations.Params, Pageable<Location>> {

    override suspend operator fun invoke(params: Params): Pageable<Location> =
        locationsRepository.getLocations(
            params.page,
            params.name,
            params.type,
            params.dimension
        ).let { pagedResponse ->
            pagedResponse.results.map { locationModel ->
                Location(
                    locationModel.id,
                    locationModel.name,
                    locationModel.type,
                    locationModel.dimension
                )
            }.let { locations ->
                Pageable(
                    pagedResponse.pages,
                    locations
                )
            }
        }

    data class Params(
        val page: Int,
        val name: String? = null,
        val type: String? = null,
        val dimension: String? = null
    )
}
