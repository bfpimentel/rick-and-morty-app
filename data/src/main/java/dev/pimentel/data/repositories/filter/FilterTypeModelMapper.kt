package dev.pimentel.data.repositories.filter

import dev.pimentel.data.models.FilterModel
import dev.pimentel.domain.models.FilterModel as DomainFilterModel

interface FilterTypeModelMapper {
    fun mapToData(type: DomainFilterModel.Type): FilterModel.Type
}

class FilterTypeModelMapperImpl : FilterTypeModelMapper {

    override fun mapToData(type: dev.pimentel.domain.models.FilterModel.Type): FilterModel.Type =
        when (type) {
            DomainFilterModel.Type.CHARACTER_NAME -> FilterModel.Type.CHARACTER_NAME
            DomainFilterModel.Type.CHARACTER_SPECIES -> FilterModel.Type.CHARACTER_SPECIES
            DomainFilterModel.Type.LOCATION_NAME -> FilterModel.Type.LOCATION_NAME
            DomainFilterModel.Type.LOCATION_TYPE -> FilterModel.Type.LOCATION_TYPE
            DomainFilterModel.Type.LOCATION_DIMENSION -> FilterModel.Type.LOCATION_DIMENSION
            DomainFilterModel.Type.EPISODE_NAME -> FilterModel.Type.EPISODE_NAME
            DomainFilterModel.Type.EPISODE_NUMBER -> FilterModel.Type.EPISODE_NUMBER
        }
}
