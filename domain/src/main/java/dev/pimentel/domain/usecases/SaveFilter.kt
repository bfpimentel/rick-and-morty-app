package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.FilterModel
import dev.pimentel.domain.repositories.FilterRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.rxjava3.core.Completable

class SaveFilter(
    private val filterRepository: FilterRepository
) : UseCase<SaveFilter.Params, Completable> {

    override fun invoke(params: Params): Completable =
        filterRepository.saveFilter(
            FilterModel(
                params.value,
                params.type
            )
        )

    data class Params(
        val value: String,
        val type: FilterModel.Type
    )
}
