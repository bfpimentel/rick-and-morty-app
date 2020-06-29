package dev.pimentel.rickandmorty.presentation.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.entities.Episode
import dev.pimentel.domain.usecases.GetEpisodes
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesState
import dev.pimentel.rickandmorty.presentation.episodes.filter.EpisodesFilterFragment
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter
import dev.pimentel.rickandmorty.presentation.episodes.mappers.EpisodesItemMapper
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.navigator.NavigatorRouter
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider

class EpisodesViewModel(
    private val getEpisodes: GetEpisodes,
    private val episodesItemMapper: EpisodesItemMapper,
    private val getErrorMessage: GetErrorMessage,
    private val navigator: NavigatorRouter,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    EpisodesContract.ViewModel {

    private var page: Int = DEFAULT_PAGE
    private var lastPage: Int = DEFAULT_LAST_PAGE
    private var lastFilter = EpisodesFilter.NO_FILTER

    private var episodes: MutableList<Episode> = mutableListOf()

    private val episodesState = MutableLiveData<EpisodesState>()
    private val filterIcon = MutableLiveData<Int>()

    override fun episodesState(): LiveData<EpisodesState> = episodesState
    override fun filterIcon(): LiveData<Int> = filterIcon

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    override fun getEpisodes(filter: EpisodesFilter) {
        if (lastFilter != filter) {
            page = FIRST_PAGE
            lastPage = DEFAULT_LAST_PAGE
            lastFilter = filter

            this.episodes = mutableListOf()
            episodesState.postValue(EpisodesState.Empty())
        } else {
            if (page == lastPage) return
            page++
        }

        handleFilterIconChange()

        getEpisodes(
            GetEpisodes.Params(
                page,
                filter.name,
                filter.number
            )
        ).compose(observeOnUIAfterSingleResult())
            .handle({ response ->
                this.episodes.addAll(response.episodes)
                this.lastPage = response.pages

                episodesState.postValue(
                    EpisodesState.Success(
                        episodesItemMapper.getAll(episodes)
                    )
                )
            }, { throwable ->
                page = DEFAULT_PAGE
                lastPage = DEFAULT_LAST_PAGE

                episodesState.postValue(
                    EpisodesState.Error(
                        getErrorMessage(GetErrorMessage.Params(throwable))
                    )
                )
            })
    }

    override fun getEpisodesWithLastFilter() {
        getEpisodes(lastFilter)
    }

    override fun openFilters() {
        navigator.navigate(
            R.id.episodes_to_episodes_filter,
            EpisodesFilterFragment.EPISODES_FILTER_ARGUMENT_KEY to lastFilter
        )
    }

    private fun handleFilterIconChange() {
        filterIcon.postValue(
            if (lastFilter != EpisodesFilter.NO_FILTER) R.drawable.ic_filter_selected
            else R.drawable.ic_filter_default
        )
    }

    private companion object {
        const val DEFAULT_PAGE = 0
        const val FIRST_PAGE = 1
        const val DEFAULT_LAST_PAGE = -1
    }
}
