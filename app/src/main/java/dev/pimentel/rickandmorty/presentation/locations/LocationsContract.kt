package dev.pimentel.rickandmorty.presentation.locations

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsItem
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter

interface LocationsContract {

    interface ViewModel {
        fun getLocations(filter: LocationsFilter)
        fun getMoreLocations()
        fun openFilters()

        fun locations(): LiveData<List<LocationsItem>>
        fun filterIcon(): LiveData<Int>
    }
}
