package dev.pimentel.rickandmorty.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.entities.Location
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsItem
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.navigator.NavigatorRouter
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider

class LocationsViewModel(
    private val navigator: NavigatorRouter,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    LocationsContract.ViewModel {

    private var page: Int = DEFAULT_PAGE
    private var lastPage: Int = DEFAULT_LAST_PAGE
    private var lastFilter = LocationsFilter.NO_FILTER

    // going to be needed later
    private var locations: MutableList<Location> = mutableListOf()

    private val locationsItems = MutableLiveData<List<LocationsItem>>()
    private val filterIcon = MutableLiveData<Int>()

    override fun locations(): LiveData<List<LocationsItem>> = locationsItems
    override fun filterIcon(): LiveData<Int> = filterIcon

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    override fun getLocations(filter: LocationsFilter) {
        TODO("Not yet implemented")
    }

    override fun getMoreLocations() {
        TODO("Not yet implemented")
    }

    override fun openFilters() {
        TODO("Not yet implemented")
    }

    private companion object {
        const val DEFAULT_PAGE = 0
        const val FIRST_PAGE = 1
        const val DEFAULT_LAST_PAGE = -1
    }
}
