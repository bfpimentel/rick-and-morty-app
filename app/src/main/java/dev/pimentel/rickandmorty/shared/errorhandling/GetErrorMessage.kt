package dev.pimentel.rickandmorty.shared.errorhandling

import android.content.Context
import dev.pimentel.domain.usecases.shared.UseCase
import dev.pimentel.rickandmorty.R
import retrofit2.HttpException
import java.io.IOException

class GetErrorMessage(
    private val context: Context
) : UseCase<GetErrorMessage.Params, String> {

    override fun invoke(params: Params): String = when (params.throwable) {
        is IOException -> context.getString(R.string.error_message_no_connection)
        is HttpException -> getHttpExceptionMessage(params.throwable)
        else -> getDefaultMessage()
    }

    private fun getHttpExceptionMessage(exception: HttpException): String =
        when (exception.code()) {
            404 -> context.getString(R.string.error_message_http_404)
            else -> getDefaultMessage()
        }

    private fun getDefaultMessage(): String = context.getString(R.string.error_message_default)

    data class Params(val throwable: Throwable)
}
