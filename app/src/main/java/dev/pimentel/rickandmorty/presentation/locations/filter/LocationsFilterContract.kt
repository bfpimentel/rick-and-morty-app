package dev.pimentel.rickandmorty.presentation.locations.filter

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilterState

interface LocationsFilterContract {

    interface ViewModel {
        fun initializeWithFilter(locationsFilter: LocationsFilter)
        fun openNameFilter()
        fun openTypeFilter()
        fun openDimensionFilter()
        fun setTextFilter(filterResult: FilterResult)
        fun clearFilter()
        fun getFilter()

        fun locationsFilterState(): LiveData<LocationsFilterState>
        fun filteringResult(): LiveData<LocationsFilter>
    }
}
