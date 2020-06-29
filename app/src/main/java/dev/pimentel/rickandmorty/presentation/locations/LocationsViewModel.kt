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
    LocationsContract.ViewModel {

    private var page: Int = DEFAULT_PAGE
    private var lastPage: Int = DEFAULT_LAST_PAGE
    private var lastFilter = LocationsFilter.NO_FILTER

    private var locations: MutableList<Location> = mutableListOf()

    private val locationsState = MutableLiveData<LocationsState>()
    private val filterIcon = MutableLiveData<Int>()

    override fun locationsState(): LiveData<LocationsState> = locationsState
    override fun filterIcon(): LiveData<Int> = filterIcon

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    override fun getLocations(filter: LocationsFilter) {
        if (lastFilter != filter) {
            page = FIRST_PAGE
            lastPage = DEFAULT_LAST_PAGE
            lastFilter = filter

            this.locations = mutableListOf()
            locationsState.postValue(LocationsState.Empty())
        } else {
            if (page == lastPage) return
            page++
        }

        handleFilterIconChange()

        getLocations(
            GetLocations.Params(
                page,
                filter.name,
                filter.type,
                filter.dimension
            )
        ).compose(observeOnUIAfterSingleResult())
            .handle({ response ->
                this.locations.addAll(response.locations)
                this.lastPage = response.pages

                locationsState.postValue(
                    LocationsState.Success(
                        locations.map(locationsItemMapper::get)
                    )
                )
            }, { throwable ->
                page = DEFAULT_PAGE
                lastPage = DEFAULT_LAST_PAGE

                locationsState.postValue(
                    LocationsState.Error(
                        getErrorMessage(GetErrorMessage.Params(throwable))
                    )
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

    private fun handleFilterIconChange() {
        filterIcon.postValue(
            if (lastFilter != LocationsFilter.NO_FILTER) R.drawable.ic_filter_selected
            else R.drawable.ic_filter_default
        )
    }

    private companion object {
        const val DEFAULT_PAGE = 0
        const val FIRST_PAGE = 1
        const val DEFAULT_LAST_PAGE = -1
    }
}
