package dev.pimentel.rickandmorty.presentation.locations

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pimentel.domain.entities.Location
import dev.pimentel.domain.usecases.GetLocations
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsIntent
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsState
import dev.pimentel.rickandmorty.presentation.locations.filter.LocationsFilterFragment
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter
import dev.pimentel.rickandmorty.presentation.locations.mappers.LocationsItemMapper
import dev.pimentel.rickandmorty.shared.dispatchersprovider.DispatchersProvider
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.extensions.throttleFirst
import dev.pimentel.rickandmorty.shared.helpers.PagingHelper
import dev.pimentel.rickandmorty.shared.helpers.PagingHelperImpl
import dev.pimentel.rickandmorty.shared.mvi.ReactiveViewModel
import dev.pimentel.rickandmorty.shared.mvi.Reducer
import dev.pimentel.rickandmorty.shared.mvi.ReducerImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LocationsViewModel @ViewModelInject constructor(
    private val getLocations: GetLocations,
    private val locationsItemMapper: LocationsItemMapper,
    private val getErrorMessage: GetErrorMessage,
    private val navigator: Navigator,
    dispatchersProvider: DispatchersProvider
) : ViewModel(), ReactiveViewModel<LocationsIntent, LocationsState>,
    PagingHelper<Location> by PagingHelperImpl(),
    Reducer<LocationsState> by ReducerImpl(LocationsState()) {

    private var lastFilter = LocationsFilter.NO_FILTER

    override val intentChannel: Channel<LocationsIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            intentChannel.consumeAsFlow().throttleFirst().collect { intent ->
                when (intent) {
                    is LocationsIntent.GetLocations -> getLocations(intent.filter)
                    is LocationsIntent.GetLocationsWithLastFilter -> getLocations(lastFilter)
                    is LocationsIntent.OpenFilters -> openFilters()
                }
            }
        }
    }

    override fun state(): StateFlow<LocationsState> = mutableState

    private suspend fun getLocations(filter: LocationsFilter) {
        handleFilterIconChange(filter)

        val reset = lastFilter != filter
        if (reset) {
            this.lastFilter = filter
            updateState { copy(scrollToTheTop = Unit, locations = emptyList()) }
        }

        handlePaging(
            request = {
                getLocations(
                    GetLocations.Params(
                        getCurrentPage(reset),
                        filter.name,
                        filter.type,
                        filter.dimension
                    )
                )
            },
            onSuccess = { result ->
                val locations = result.map(locationsItemMapper::get)
                updateState { copy(locations = locations) }
            },
            onError = { error ->
                val errorMessage = getErrorMessage(GetErrorMessage.Params(error))
                updateState { copy(errorMessage = errorMessage) }
            }
        )
    }

    private suspend fun handleFilterIconChange(filter: LocationsFilter) {
        updateState {
            copy(
                filterIcon = if (filter != LocationsFilter.NO_FILTER) R.drawable.ic_filter_selected
                else R.drawable.ic_filter_default
            )
        }
    }

    private fun openFilters() {
        navigator.navigate(
            R.id.locations_to_locations_filter,
            LocationsFilterFragment.LOCATIONS_FILTER_ARGUMENT_KEY to lastFilter
        )
    }
}
