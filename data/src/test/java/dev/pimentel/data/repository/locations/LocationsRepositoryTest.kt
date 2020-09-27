package dev.pimentel.data.repository.locations

import dev.pimentel.data.dto.LocationDTO
import dev.pimentel.data.dto.PagedResponseDTO
import dev.pimentel.data.repositories.locations.LocationsRepositoryImpl
import dev.pimentel.data.sources.remote.LocationsRemoteDataSource
import dev.pimentel.domain.repositories.LocationsRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import dev.pimentel.domain.models.LocationModel as DomainLocationModel
import dev.pimentel.domain.models.PagedResponseModel as DomainPagedResponse

class LocationsRepositoryTest {

    private val locationsRemoteDataSource = mockk<LocationsRemoteDataSource>()
    private lateinit var locationsRepository: LocationsRepository

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        locationsRepository = LocationsRepositoryImpl(locationsRemoteDataSource)

        assertNotNull(locationsRepository)
    }

    @Test
    fun `should get locations`() {
        val dataResponse = PagedResponseDTO(
            PagedResponseDTO.Info(10),
            listOf(
                LocationDTO(1, "name1", "type1", "dimension1"),
                LocationDTO(2, "name2", "type2", "dimension2")
            )
        )

        val domainResponse = DomainPagedResponse(
            10,
            listOf(
                DomainLocationModel(1, "name1", "type1", "dimension1"),
                DomainLocationModel(2, "name2", "type2", "dimension2")
            )
        )

        val page = 1
        val name = "name"
        val type = "type"
        val dimension = "dimension"

        every {
            locationsRemoteDataSource.getLocations(page, name, type, dimension)
        } returns Single.just(dataResponse)

        locationsRepository.getLocations(page, name, type, dimension)
            .test()
            .assertResult(domainResponse)
            .assertNoErrors()

        verify(exactly = 1) {
            locationsRemoteDataSource.getLocations(page, name, type, dimension)
        }
        confirmVerified(locationsRemoteDataSource)
    }
}
