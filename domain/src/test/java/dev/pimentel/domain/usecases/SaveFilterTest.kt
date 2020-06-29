package dev.pimentel.domain.usecases

import dev.pimentel.domain.models.FilterModel
import dev.pimentel.domain.repositories.FilterRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveFilterTest {

    private val filterRepository = mockk<FilterRepository>()
    private lateinit var saveFilter: SaveFilter

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        saveFilter = SaveFilter(filterRepository)
    }

    @Test
    fun `should save filter and then just complete`() {
        val value = "value"
        val type = FilterModel.Type.LOCATION_TYPE

        val filterModel = FilterModel(value, type)

        every { filterRepository.saveFilter(filterModel) } returns Completable.complete()

        saveFilter(SaveFilter.Params(value, type))
            .test()
            .assertComplete()
            .assertNoErrors()

        verify(exactly = 1) { filterRepository.saveFilter(filterModel) }
        confirmVerified(filterRepository)
    }
}
