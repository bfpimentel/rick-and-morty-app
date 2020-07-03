package dev.pimentel.rickandmorty.presentation.locations.mappers

import dev.pimentel.domain.entities.Location
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsItem

interface LocationsItemMapper {
    fun get(location: Location): LocationsItem
}

class LocationsItemMapperImpl : LocationsItemMapper {

    override fun get(location: Location): LocationsItem =
        LocationsItem(
            location.id,
            location.type,
            location.name
        )
}
