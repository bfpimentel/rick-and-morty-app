package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.FilterModel
import dev.pimentel.domain.repositories.FilterRepository
import dev.pimentel.domain.usecases.shared.UseCase

class SaveFilter(
    private val filterRepository: FilterRepository
) : UseCase<SaveFilter.Params, Unit> {

    override suspend fun invoke(params: Params) =
        filterRepository.saveFilter(
            params.value,
            params.type
        )

    data class Params(
        val value: String,
        val type: FilterModel.Type
    )
}
