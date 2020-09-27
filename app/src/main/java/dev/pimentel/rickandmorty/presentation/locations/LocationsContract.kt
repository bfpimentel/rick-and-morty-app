package dev.pimentel.rickandmorty.presentation.locations

import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsIntent
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsState
import dev.pimentel.rickandmorty.shared.mvi.ReactiveViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
interface LocationsContract {

    interface ViewModel : ReactiveViewModel<LocationsIntent, LocationsState>
}
