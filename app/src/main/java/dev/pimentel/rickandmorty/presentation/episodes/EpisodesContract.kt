package dev.pimentel.rickandmorty.presentation.episodes

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter

interface EpisodesContract {

    interface ViewModel {
        fun getEpisodes(filter: EpisodesFilter)
        fun getMoreEpisodes()
        fun openFilters()

        fun episodes(): LiveData<List<EpisodesItem>>
        fun filterIcon(): LiveData<Int>
    }
}
