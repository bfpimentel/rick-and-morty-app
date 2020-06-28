package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.FilterModel
import dev.pimentel.domain.repositories.FilterRepository
import dev.pimentel.domain.usecases.shared.UseCase
import io.reactivex.rxjava3.core.Completable

class GetFilters(
    private val filterRepository: FilterRepository
) : UseCase<GetFilters.Params, Completable> {

    override fun invoke(params: Params): Completable =
        Completable.fromSingle(filterRepository.getFiltersByType(params.type))

    data class Params(
        val type: FilterModel.Type
    )
}
