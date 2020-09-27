package dev.pimentel.domain.usecases

import dev.pimentel.domain.entities.Location
import dev.pimentel.domain.entities.Pageable
import dev.pimentel.domain.models.LocationModel
import dev.pimentel.domain.models.PagedResponseModel
import dev.pimentel.domain.repositories.LocationsRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLocationsTest {

    private val locationsRepository = mockk<LocationsRepository>()
    private lateinit var getLocations: GetLocations

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        getLocations = GetLocations(locationsRepository)

        assertNotNull(getLocations)
    }

    @Test
    fun `should get locations and then return the mapped response`() {
        val page = 1
        val name = "name"
        val type = "type"
        val dimension = "dimension"

        val response = PagedResponseModel(
            10,
            listOf(
                LocationModel(1, "name1", "type1", "dimension1"),
                LocationModel(2, "name2", "type2", "dimension2")
            )
        )

        val result = Pageable(
            10,
            listOf(
                Location(1, "name1", "type1", "dimension1"),
                Location(2, "name2", "type2", "dimension2")
            )
        )

        every {
            locationsRepository.getLocations(page, name, type, dimension)
        } returns Single.just(response)

        getLocations(GetLocations.Params(page, name, type, dimension))
            .test()
            .assertResult(result)
            .assertNoErrors()

        verify(exactly = 1) {
            locationsRepository.getLocations(page, name, type, dimension)
        }
        confirmVerified(locationsRepository)
    }
}
