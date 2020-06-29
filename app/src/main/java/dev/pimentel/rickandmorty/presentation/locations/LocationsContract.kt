package dev.pimentel.rickandmorty.presentation.locations

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsState
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter

interface LocationsContract {

    interface ViewModel {
        fun getLocations(filter: LocationsFilter)
        fun getLocationsWithLastFilter()
        fun openFilters()

        fun locationsState(): LiveData<LocationsState>
        fun filterIcon(): LiveData<Int>
    }
}
