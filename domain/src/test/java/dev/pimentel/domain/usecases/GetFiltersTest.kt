package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.FilterModel
import dev.pimentel.domain.repositories.FilterRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetFiltersTest {

    private val filterRepository = mockk<FilterRepository>()
    private lateinit var getFilters: GetFilters

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        getFilters = GetFilters(filterRepository)
    }

    @Test
    fun `should get filters`() {
        val type = FilterModel.Type.LOCATION_TYPE

        val response = listOf("value1", "value2", "value3")

        every { filterRepository.getFiltersByType(type) } returns Single.just(response)

        getFilters(GetFilters.Params(type))
            .test()
            .assertResult(listOf("value3", "value2", "value1"))
            .assertNoErrors()

        verify(exactly = 1) { filterRepository.getFiltersByType(type) }
        confirmVerified(filterRepository)
    }
}
