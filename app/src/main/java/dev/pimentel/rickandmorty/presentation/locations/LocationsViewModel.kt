package dev.pimentel.rickandmorty.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.entities.Location
import dev.pimentel.domain.usecases.GetLocations
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsState
import dev.pimentel.rickandmorty.presentation.locations.filter.LocationsFilterFragment
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter
import dev.pimentel.rickandmorty.presentation.locations.mappers.LocationsItemMapper
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.helpers.PagingHelper
import dev.pimentel.rickandmorty.shared.helpers.PagingHelperImpl
import dev.pimentel.rickandmorty.shared.navigator.NavigatorRouter
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider

class LocationsViewModel(
    private val getLocations: GetLocations,
    private val locationsItemMapper: LocationsItemMapper,
    private val getErrorMessage: GetErrorMessage,
    private val navigator: NavigatorRouter,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    PagingHelper<Location> by PagingHelperImpl(schedulerProvider),
    LocationsContract.ViewModel {

    private var lastFilter = LocationsFilter.NO_FILTER

    private val locationsState = MutableLiveData<LocationsState>()
    private val filterIcon = MutableLiveData<Int>()

    override fun locationsState(): LiveData<LocationsState> = locationsState
    override fun filterIcon(): LiveData<Int> = filterIcon

    override fun onCleared() {
        super.onCleared()
        disposeHolder()
        disposePaging()
    }

    override fun getLocations(filter: LocationsFilter) {
        handleFilterIconChange(filter)

        val reset = lastFilter != filter
        if (reset) {
            this.lastFilter = filter
            locationsState.postValue(LocationsState.Empty())
        }

        getLocations(
            GetLocations.Params(
                getCurrentPage(reset),
                filter.name,
                filter.type,
                filter.dimension
            )
        ).handlePaging({ result ->
            locationsState.postValue(
                LocationsState.Success(result.map(locationsItemMapper::get))
            )
        }, { throwable ->
            locationsState.postValue(
                LocationsState.Error(getErrorMessage(GetErrorMessage.Params(throwable)))
            )
        })
    }

    override fun getLocationsWithLastFilter() {
        getLocations(lastFilter)
    }

    override fun openFilters() {
        navigator.navigate(
            R.id.locations_to_locations_filter,
            LocationsFilterFragment.LOCATIONS_FILTER_ARGUMENT_KEY to lastFilter
        )
    }

    private fun handleFilterIconChange(filter: LocationsFilter) {
        filterIcon.postValue(
            if (filter != LocationsFilter.NO_FILTER) R.drawable.ic_filter_selected
            else R.drawable.ic_filter_default
        )
    }
}
