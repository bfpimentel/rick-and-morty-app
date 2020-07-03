package dev.pimentel.rickandmorty.shared.errorhandling

import android.content.Context
import dev.pimentel.rickandmorty.R
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class GetErrorMessageTest {

    private val context = mockk<Context>(relaxed = true)
    private lateinit var useCase: GetErrorMessage

    @BeforeEach
    @Test
    fun `should setup subject and it must not be null`() {
        useCase = GetErrorMessage(context)
        assertNotNull(useCase)
    }

    @Test
    fun `should return no connection error type when throwable is an instance of IOException`() {
        val message = "message"

        every { context.getString(R.string.error_message_no_connection) } returns message

        assertEquals(
            message,
            useCase(GetErrorMessage.Params(IOException()))
        )

        verify(exactly = 1) { context.getString(R.string.error_message_no_connection) }
        confirmVerified(context)
    }

    @Test
    fun `should return no results message when throwable is a http exception with code 404`() {
        val message = "message"
        val throwable = HttpException(Response.error<Any>(404, mockk(relaxed = true)))

        every { context.getString(R.string.error_message_http_404) } returns message

        assertEquals(
            message,
            useCase(GetErrorMessage.Params(throwable))
        )

        verify(exactly = 1) { context.getString(R.string.error_message_http_404) }
        confirmVerified(context)
    }

    @Test
    fun `should return default error message when throwable is HttpException and code is not mapped`() {
        val message = "message"
        val throwable = HttpException(Response.error<Any>(400, mockk(relaxed = true)))

        every { context.getString(R.string.error_message_default) } returns message

        assertEquals(
            message,
            useCase(GetErrorMessage.Params(throwable))
        )

        verify(exactly = 1) { context.getString(R.string.error_message_default) }
        confirmVerified(context)
    }

    @Test
    fun `should return default error type when throwable is not a mapped throwable type`() {
        val message = "message"

        every { context.getString(R.string.error_message_default) } returns message

        assertEquals(
            message,
            useCase(GetErrorMessage.Params(IllegalArgumentException()))
        )

        verify(exactly = 1) { context.getString(R.string.error_message_default) }
        confirmVerified(context)
    }

    @Test
    fun `GetErrorTypeParams must contain a non null throwable`() {
        val throwable = IllegalArgumentException()

        val params = GetErrorMessage.Params(throwable)

        assertNotNull(params.throwable)
        assertEquals(params.throwable, throwable)
    }
}
