package dev.pimentel.rickandmorty.shared.errorhandling

import android.content.Context
import dev.pimentel.domain.usecases.shared.UseCase

class GetErrorMessage(
    private val context: Context
) : UseCase<GetErrorMessage.Params, String> {

    override fun invoke(params: Params): String = ""

    data class Params(val throwable: Throwable)
}
