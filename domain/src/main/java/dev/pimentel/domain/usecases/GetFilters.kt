package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.FilterModel
import dev.pimentel.domain.repositories.FilterRepository
import dev.pimentel.domain.usecases.shared.UseCase

class GetFilters(
    private val filterRepository: FilterRepository
) : UseCase<GetFilters.Params, List<String>> {

    override suspend fun invoke(params: Params): List<String> =
        filterRepository.getFiltersByType(params.type).asReversed()

    data class Params(
        val type: FilterModel.Type
    )
}
