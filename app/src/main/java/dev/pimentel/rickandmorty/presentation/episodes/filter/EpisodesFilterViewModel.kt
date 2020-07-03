package dev.pimentel.rickandmorty.presentation.episodes.filter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilterState
import dev.pimentel.rickandmorty.presentation.filter.FilterDialog
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.shared.navigator.Navigator

@Suppress("TooManyFunctions")
class EpisodesFilterViewModel @ViewModelInject constructor(
    private val navigator: Navigator
) : ViewModel(),
    EpisodesFilterContract.ViewModel {

    private lateinit var lastFilter: EpisodesFilter
    private lateinit var currentFilter: EpisodesFilter

    private val episodesFilterState = MutableLiveData<EpisodesFilterState>()
    private val filteringResult = MutableLiveData<EpisodesFilter>()

    override fun episodesFilterState(): LiveData<EpisodesFilterState> = episodesFilterState
    override fun filteringResult(): LiveData<EpisodesFilter> = filteringResult

    override fun initializeWithFilter(episodesFilter: EpisodesFilter) {
        this.lastFilter = episodesFilter
        this.currentFilter = episodesFilter.copy()
        buildFilterState()
    }

    override fun setTextFilter(filterResult: FilterResult) {
        when (filterResult.type) {
            FilterType.EPISODE_NAME -> setName(filterResult.value)
            FilterType.EPISODE_NUMBER -> setNumber(filterResult.value)
            else -> throw IllegalArgumentException("Illegal Filter Type: ${filterResult.type}")
        }
    }

    override fun openNameFilter() {
        navigator.navigate(
            R.id.episodes_filter_to_filter,
            FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.EPISODE_NAME
        )
    }

    override fun openNumberFilter() {
        navigator.navigate(
            R.id.episodes_filter_to_filter,
            FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.EPISODE_NUMBER
        )
    }

    override fun clearFilter() {
        this.currentFilter = EpisodesFilter.NO_FILTER
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

    private fun setNumber(number: String) {
        this.currentFilter = this.currentFilter.copy(
            number = number
        )
        buildFilterState()
    }

    private fun buildFilterState() {
        episodesFilterState.postValue(
            EpisodesFilterState(
                currentFilter != lastFilter,
                currentFilter != EpisodesFilter.NO_FILTER,
                currentFilter.name,
                currentFilter.number
            )
        )
    }
}
