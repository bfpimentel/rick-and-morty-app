package dev.pimentel.domain.usecases.shared

interface UseCase<Params, Result> {
    operator fun invoke(params: Params): Result
}

object NoParams
