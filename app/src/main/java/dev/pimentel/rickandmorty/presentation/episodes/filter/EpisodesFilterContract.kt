package dev.pimentel.rickandmorty.presentation.episodes.filter

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilterState
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult

interface EpisodesFilterContract {

    interface ViewModel {
        fun initializeWithFilter(episodesFilter: EpisodesFilter)
        fun openNameFilter()
        fun openNumberFilter()
        fun setTextFilter(filterResult: FilterResult)
        fun clearFilter()
        fun getFilter()

        fun episodesFilterState(): LiveData<EpisodesFilterState>
        fun filteringResult(): LiveData<EpisodesFilter>
    }
}
