package dev.pimentel.rickandmorty.presentation.episodes

import androidx.hilt.lifecycle.ViewModelInject
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
import dev.pimentel.rickandmorty.shared.dispatchersprovider.DispatchersProvider
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.helpers.PagingHelper
import dev.pimentel.rickandmorty.shared.helpers.PagingHelperImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider

class EpisodesViewModel @ViewModelInject constructor(
    private val getEpisodes: GetEpisodes,
    private val episodesItemMapper: EpisodesItemMapper,
    private val getErrorMessage: GetErrorMessage,
    private val navigator: Navigator,
    dispatchersProvider: DispatchersProvider
) : ViewModel(),
    PagingHelper<Episode> by PagingHelperImpl(),
    EpisodesContract.ViewModel {

    private var lastFilter = EpisodesFilter.NO_FILTER

    private val episodesState = MutableLiveData<EpisodesState>()
    private val filterIcon = MutableLiveData<Int>()

    override fun episodesState(): LiveData<EpisodesState> = episodesState
    override fun filterIcon(): LiveData<Int> = filterIcon

    override fun getEpisodes(filter: EpisodesFilter) {
        handleFilterIconChange(filter)

        val reset = lastFilter != filter
        if (reset) {
            this.lastFilter = filter
            episodesState.postValue(EpisodesState.Empty())
        }

        handlePaging(
            request = {
                getEpisodes(
                    GetEpisodes.Params(
                        getCurrentPage(reset),
                        filter.name,
                        filter.number
                    )
                )
            },
            onSuccess = { response ->
                episodesState.postValue(
                    EpisodesState.Success(episodesItemMapper.getAll(response))
                )
            },
            onError = { error ->
                episodesState.postValue(
                    EpisodesState.Error(getErrorMessage(GetErrorMessage.Params(error)))
                )
            }
        )
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

    private fun handleFilterIconChange(filter: EpisodesFilter) {
        filterIcon.postValue(
            if (filter != EpisodesFilter.NO_FILTER) R.drawable.ic_filter_selected
            else R.drawable.ic_filter_default
        )
    }
}
