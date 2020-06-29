package dev.pimentel.data.repository.filter

import dev.pimentel.data.models.FilterModel
import dev.pimentel.data.repositories.filter.FilterTypeModelMapper
import dev.pimentel.data.repositories.filter.FilterTypeModelMapperImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import dev.pimentel.domain.models.FilterModel as DomainFilterModel

class FilterTypeModelMapperTest {

    private lateinit var filterTypeModelMapper: FilterTypeModelMapper

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        filterTypeModelMapper = FilterTypeModelMapperImpl()

        assertNotNull(filterTypeModelMapper)
    }

    @Test
    fun `should return data types when inputting domain types`() {
        assertEquals(
            filterTypeModelMapper.mapToData(DomainFilterModel.Type.CHARACTER_NAME),
            FilterModel.Type.CHARACTER_NAME
        )

        assertEquals(
            filterTypeModelMapper.mapToData(DomainFilterModel.Type.CHARACTER_SPECIES),
            FilterModel.Type.CHARACTER_SPECIES
        )

        assertEquals(
            filterTypeModelMapper.mapToData(DomainFilterModel.Type.LOCATION_NAME),
            FilterModel.Type.LOCATION_NAME
        )

        assertEquals(
            filterTypeModelMapper.mapToData(DomainFilterModel.Type.LOCATION_TYPE),
            FilterModel.Type.LOCATION_TYPE
        )

        assertEquals(
            filterTypeModelMapper.mapToData(DomainFilterModel.Type.LOCATION_DIMENSION),
            FilterModel.Type.LOCATION_DIMENSION
        )

        assertEquals(
            filterTypeModelMapper.mapToData(DomainFilterModel.Type.EPISODE_NAME),
            FilterModel.Type.EPISODE_NAME
        )

        assertEquals(
            filterTypeModelMapper.mapToData(DomainFilterModel.Type.EPISODE_NUMBER),
            FilterModel.Type.EPISODE_NUMBER
        )
    }
}
