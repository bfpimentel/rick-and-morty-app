package dev.pimentel.data.repositories.filter

import dev.pimentel.data.dto.FilterDTO
import dev.pimentel.domain.models.FilterModel

interface FilterTypeModelMapper {
    fun mapToData(type: FilterModel.Type): FilterDTO.Type
}

class FilterTypeModelMapperImpl : FilterTypeModelMapper {

    override fun mapToData(type: FilterModel.Type): FilterDTO.Type =
        when (type) {
            FilterModel.Type.CHARACTER_NAME -> FilterDTO.Type.CHARACTER_NAME
            FilterModel.Type.CHARACTER_SPECIES -> FilterDTO.Type.CHARACTER_SPECIES
            FilterModel.Type.LOCATION_NAME -> FilterDTO.Type.LOCATION_NAME
            FilterModel.Type.LOCATION_TYPE -> FilterDTO.Type.LOCATION_TYPE
            FilterModel.Type.LOCATION_DIMENSION -> FilterDTO.Type.LOCATION_DIMENSION
            FilterModel.Type.EPISODE_NAME -> FilterDTO.Type.EPISODE_NAME
            FilterModel.Type.EPISODE_NUMBER -> FilterDTO.Type.EPISODE_NUMBER
        }
}
