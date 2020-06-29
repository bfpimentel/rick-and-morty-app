package dev.pimentel.rickandmorty.presentation.filter

import dev.pimentel.domain.models.FilterModel
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapper
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapperImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FilterTypeMapperTest {

    private lateinit var filterTypeMapper: FilterTypeMapper

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        filterTypeMapper = FilterTypeMapperImpl()

        assertNotNull(filterTypeMapper)
    }

    @Test
    fun `should map filter types to domain`() {
        assertEquals(
            filterTypeMapper.mapToDomain(FilterType.CHARACTER_NAME),
            FilterModel.Type.CHARACTER_NAME
        )

        assertEquals(
            filterTypeMapper.mapToDomain(FilterType.CHARACTER_SPECIES),
            FilterModel.Type.CHARACTER_SPECIES
        )

        assertEquals(
            filterTypeMapper.mapToDomain(FilterType.LOCATION_NAME),
            FilterModel.Type.LOCATION_NAME
        )

        assertEquals(
            filterTypeMapper.mapToDomain(FilterType.LOCATION_TYPE),
            FilterModel.Type.LOCATION_TYPE
        )

        assertEquals(
            filterTypeMapper.mapToDomain(FilterType.LOCATION_DIMENSION),
            FilterModel.Type.LOCATION_DIMENSION
        )

        assertEquals(
            filterTypeMapper.mapToDomain(FilterType.EPISODE_NAME),
            FilterModel.Type.EPISODE_NAME
        )

        assertEquals(
            filterTypeMapper.mapToDomain(FilterType.EPISODE_NUMBER),
            FilterModel.Type.EPISODE_NUMBER
        )
    }
}
