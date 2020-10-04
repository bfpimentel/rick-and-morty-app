package dev.pimentel.rickandmorty.presentation.characters.filter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilterIntent
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilterState
import dev.pimentel.rickandmorty.presentation.filter.FilterDialog
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.shared.dispatchersprovider.DispatchersProvider
import dev.pimentel.rickandmorty.shared.extensions.throttleFirst
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
@Suppress("TooManyFunctions")
class CharactersFilterViewModel @ViewModelInject constructor(
    private val navigator: Navigator,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel(), ReactiveViewModel<CharactersFilterIntent, CharactersFilterState>,
    Reducer<CharactersFilterState> by ReducerImpl(CharactersFilterState()) {

    private lateinit var lastFilter: CharactersFilter
    private lateinit var currentFilter: CharactersFilter

    override val intentChannel: Channel<CharactersFilterIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            intentChannel.consumeAsFlow().throttleFirst().collect { intent ->
                when (intent) {
                    is CharactersFilterIntent.Initialize -> initializeWithFilter(intent.filter)
                    is CharactersFilterIntent.OpenNameFilter -> openNameFilter()
                    is CharactersFilterIntent.OpenSpeciesFilter -> openSpeciesFilter()
                    is CharactersFilterIntent.SetTextFilter -> setTextFilter(intent.filterResult)
                    is CharactersFilterIntent.SetStatus -> setStatus(intent.selectedStatusIndex)
                    is CharactersFilterIntent.SetGender -> setGender(intent.selectedGenderIndex)
                    is CharactersFilterIntent.ClearFilter -> clearFilter()
                    is CharactersFilterIntent.ApplyFilter -> getFilter()
                }
            }
        }
    }

    override fun state(): StateFlow<CharactersFilterState> = mutableState

    private suspend fun initializeWithFilter(charactersFilter: CharactersFilter) {
        this.lastFilter = charactersFilter
        this.currentFilter = charactersFilter.copy()
        buildFilterState()
    }

    private fun openNameFilter() {
        viewModelScope.launch(dispatchersProvider.ui) {
            navigator.navigate(
                R.id.characters_filter_to_filter,
                FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.CHARACTER_NAME
            )
        }
    }

    private fun openSpeciesFilter() {
        viewModelScope.launch(dispatchersProvider.ui) {
            navigator.navigate(
                R.id.characters_filter_to_filter,
                FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.CHARACTER_SPECIES
            )
        }
    }

    private suspend fun setTextFilter(filterResult: FilterResult) {
        when (filterResult.type) {
            FilterType.CHARACTER_NAME -> setName(filterResult.value)
            FilterType.CHARACTER_SPECIES -> setSpecies(filterResult.value)
            else -> throw IllegalArgumentException("Illegal Filter Type: ${filterResult.type}")
        }
    }

    private suspend fun setStatus(selectedStatusIndex: Int) {
        this.currentFilter = this.currentFilter.copy(
            status = STATUS_LIST[selectedStatusIndex]
        )
        buildFilterState()
    }

    private suspend fun setGender(selectedGenderIndex: Int) {
        this.currentFilter = this.currentFilter.copy(
            gender = GENDER_LIST[selectedGenderIndex]
        )
        buildFilterState()
    }

    private suspend fun setName(name: String) {
        this.currentFilter = this.currentFilter.copy(
            name = name
        )
        buildFilterState()
    }

    private suspend fun setSpecies(species: String) {
        this.currentFilter = this.currentFilter.copy(
            species = species
        )
        buildFilterState()
    }

    private suspend fun clearFilter() {
        this.currentFilter = CharactersFilter.NO_FILTER
        buildFilterState()
    }

    private suspend fun getFilter() {
        updateState { copy(result = currentFilter) }
        viewModelScope.launch(dispatchersProvider.ui) { navigator.pop() }
    }

    private suspend fun buildFilterState() {
        updateState {
            copy(
                canApplyFilter = lastFilter != currentFilter,
                canClear = currentFilter != CharactersFilter.NO_FILTER,
                name = currentFilter.name,
                species = currentFilter.species,
                selectedStatusIndex = STATUS_LIST
                    .indexOfFirst { status -> status == currentFilter.status }
                    .takeIf { it != -1 },
                selectedGenderIndex = GENDER_LIST
                    .indexOfFirst { gender -> gender == currentFilter.gender }
                    .takeIf { it != -1 }
            )
        }
    }

    private companion object {
        val STATUS_LIST = listOf(
            "alive",
            "dead",
            "unknown"
        )
        val GENDER_LIST = listOf(
            "female",
            "male",
            "genderless",
            "unknown"
        )
    }
}
