package dev.pimentel.rickandmorty.presentation.episodes

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesState
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter

interface EpisodesContract {

    interface ViewModel {
        fun getEpisodes(filter: EpisodesFilter)
        fun getEpisodesWithLastFilter()
        fun openFilters()

        fun episodesState(): LiveData<EpisodesState>
        fun filterIcon(): LiveData<Int>
    }
}
