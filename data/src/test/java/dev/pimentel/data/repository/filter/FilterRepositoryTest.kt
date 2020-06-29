package dev.pimentel.data.repository.filter

import dev.pimentel.data.models.FilterModel
import dev.pimentel.data.repositories.filter.FilterRepositoryImpl
import dev.pimentel.data.repositories.filter.FilterTypeModelMapper
import dev.pimentel.data.sources.local.FilterLocalDataSource
import dev.pimentel.domain.repositories.FilterRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import dev.pimentel.domain.models.FilterModel as DomainFilterModel

class FilterRepositoryTest {

    private val filterLocalDataSource = mockk<FilterLocalDataSource>()
    private val filterTypeModelMapper = mockk<FilterTypeModelMapper>()
    private lateinit var filterRepository: FilterRepository

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        filterRepository = FilterRepositoryImpl(
            filterLocalDataSource,
            filterTypeModelMapper
        )

        assertNotNull(filterRepository)
    }

    @Test
    fun `should save filter and then just complete`() {
        val domainFilter = DomainFilterModel("", DomainFilterModel.Type.LOCATION_TYPE)
        val dataFilter = FilterModel("", FilterModel.Type.LOCATION_TYPE)

        every { filterTypeModelMapper.mapToData(domainFilter.type) } returns FilterModel.Type.LOCATION_TYPE
        every { filterLocalDataSource.saveFilter(dataFilter) } returns Completable.complete()

        filterRepository.saveFilter(domainFilter)
            .test()
            .assertComplete()
            .assertNoErrors()

        verify(exactly = 1) {
            filterTypeModelMapper.mapToData(domainFilter.type)
            filterLocalDataSource.saveFilter(dataFilter)
        }
        confirmVerified(filterTypeModelMapper, filterLocalDataSource)
    }

    @Test
    fun `should get all filters by type and map them to a string list`() {
        val domainType = DomainFilterModel.Type.LOCATION_TYPE
        val dataType = FilterModel.Type.LOCATION_TYPE

        val list = listOf(
            FilterModel("value1", dataType),
            FilterModel("value2", dataType),
            FilterModel("value3", dataType)
        )

        every { filterTypeModelMapper.mapToData(domainType) } returns dataType
        every { filterLocalDataSource.getFilters(dataType) } returns Single.just(list)

        filterRepository.getFiltersByType(domainType)
            .test()
            .assertResult(listOf("value1", "value2", "value3"))
            .assertNoErrors()

        verify(exactly = 1) {
            filterTypeModelMapper.mapToData(domainType)
            filterLocalDataSource.getFilters(dataType)
        }
        confirmVerified(filterTypeModelMapper, filterLocalDataSource)
    }
}
