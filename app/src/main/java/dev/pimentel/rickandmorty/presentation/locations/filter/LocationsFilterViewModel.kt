package dev.pimentel.rickandmorty.presentation.locations.filter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.filter.FilterDialog
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilterState
import dev.pimentel.rickandmorty.shared.navigator.Navigator

@Suppress("TooManyFunctions")
class LocationsFilterViewModel @ViewModelInject constructor(
    private val navigator: Navigator
) : ViewModel(),
    LocationsFilterContract.ViewModel {

    private lateinit var lastFilter: LocationsFilter
    private lateinit var currentFilter: LocationsFilter

    private val locationsFilterState = MutableLiveData<LocationsFilterState>()
    private val filteringResult = MutableLiveData<LocationsFilter>()

    override fun locationsFilterState(): LiveData<LocationsFilterState> = locationsFilterState
    override fun filteringResult(): LiveData<LocationsFilter> = filteringResult

    override fun initializeWithFilter(locationsFilter: LocationsFilter) {
        this.lastFilter = locationsFilter
        this.currentFilter = locationsFilter.copy()
        buildFilterState()
    }

    override fun setTextFilter(filterResult: FilterResult) {
        when (filterResult.type) {
            FilterType.LOCATION_NAME -> setName(filterResult.value)
            FilterType.LOCATION_TYPE -> setType(filterResult.value)
            FilterType.LOCATION_DIMENSION -> setDimension(filterResult.value)
            else -> throw IllegalArgumentException("Illegal Filter Type: ${filterResult.type}")
        }
    }

    override fun openNameFilter() {
        navigator.navigate(
            R.id.locations_filter_to_filter,
            FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.LOCATION_NAME
        )
    }

    override fun openTypeFilter() {
        navigator.navigate(
            R.id.locations_filter_to_filter,
            FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.LOCATION_TYPE
        )
    }

    override fun openDimensionFilter() {
        navigator.navigate(
            R.id.locations_filter_to_filter,
            FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.LOCATION_DIMENSION
        )
    }

    override fun clearFilter() {
        this.currentFilter = LocationsFilter.NO_FILTER
        buildFilterState()
    }

    override fun getFilter() {
        filteringResult.postValue(currentFilter)
        navigator.pop()
    }

    private fun setName(name: String) {
        this.currentFilter = this.currentFilter.copy(
            name = name
        )
        buildFilterState()
    }

    private fun setType(type: String) {
        this.currentFilter = this.currentFilter.copy(
            type = type
        )
        buildFilterState()
    }

    private fun setDimension(dimension: String) {
        this.currentFilter = this.currentFilter.copy(
            dimension = dimension
        )
        buildFilterState()
    }

    private fun buildFilterState() {
        locationsFilterState.postValue(
            LocationsFilterState(
                currentFilter != lastFilter,
                currentFilter != LocationsFilter.NO_FILTER,
                currentFilter.name,
                currentFilter.type,
                currentFilter.dimension
            )
        )
    }
}
