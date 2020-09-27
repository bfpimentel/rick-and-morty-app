package dev.pimentel.rickandmorty.presentation.filter

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterIntent
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterState
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.shared.mvi.ReactiveViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
interface FilterContract {

    interface ViewModel : ReactiveViewModel<FilterIntent, FilterState>
}
