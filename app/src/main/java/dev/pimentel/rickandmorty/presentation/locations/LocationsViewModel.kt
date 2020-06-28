package dev.pimentel.rickandmorty.presentation.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.entities.Location
import dev.pimentel.domain.usecases.GetLocations
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsItem
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter
import dev.pimentel.rickandmorty.presentation.locations.mappers.LocationsItemMapper
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.navigator.NavigatorRouter
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import timber.log.Timber

class LocationsViewModel(
    private val getLocations: GetLocations,
    private val itemMapper: LocationsItemMapper,
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
        if (lastFilter != filter) {
            page = FIRST_PAGE
            lastPage = DEFAULT_LAST_PAGE
            lastFilter = filter

            this.locations = mutableListOf()
            locationsItems.postValue(listOf())
        } else {
            if (page == lastPage) {
                return
            }

            page++
        }

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

                locationsItems.postValue(this.locations.map(itemMapper::get))
            }, Timber::e)
    }

    override fun getMoreLocations() {
        getLocations(lastFilter)
    }

    override fun openFilters() {
    }

    private companion object {
        const val DEFAULT_PAGE = 0
        const val FIRST_PAGE = 1
        const val DEFAULT_LAST_PAGE = -1
    }
}
