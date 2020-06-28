package dev.pimentel.rickandmorty.presentation.filter.mappers

import dev.pimentel.domain.models.FilterModel
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType

interface FilterTypeMapper {
    fun mapToDomain(type: FilterType): FilterModel.Type
}

class FilterTypeMapperImpl : FilterTypeMapper {

    override fun mapToDomain(type: FilterType): FilterModel.Type =
        when (type) {
            FilterType.CHARACTER_NAME -> FilterModel.Type.CHARACTER_NAME
            FilterType.CHARACTER_SPECIES -> FilterModel.Type.CHARACTER_SPECIES
            FilterType.LOCATION_NAME -> FilterModel.Type.LOCATION_NAME
            FilterType.LOCATION_TYPE -> FilterModel.Type.LOCATION_TYPE
            FilterType.LOCATION_DIMENSION -> FilterModel.Type.LOCATION_DIMENSION
            FilterType.EPISODE_NAME -> FilterModel.Type.EPISODE_NAME
            FilterType.EPISODE_NUMBER -> FilterModel.Type.EPISODE_NUMBER
        }
}
