package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Location
import dev.pimentel.domain.repositories.LocationsRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.rxjava3.core.Single

class GetLocations(
    private val locationsRepository: LocationsRepository
) : UseCase<GetLocations.Params, Single<GetLocations.Response>> {

    override fun invoke(params: Params): Single<Response> =
        locationsRepository.getLocations(
            params.page,
            params.name,
            params.type,
            params.dimension
        ).map { pagedResponse ->
            pagedResponse.results.map { locationModel ->
                Location(
                    locationModel.id,
                    locationModel.name,
                    locationModel.type,
                    locationModel.dimension
                )
            }.let { locations ->
                Response(
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

    data class Response(
        val pages: Int,
        val locations: List<Location>
    )
}
