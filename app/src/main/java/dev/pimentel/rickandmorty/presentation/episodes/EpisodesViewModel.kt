package dev.pimentel.rickandmorty.presentation.episodes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pimentel.domain.entities.Episode
import dev.pimentel.domain.usecases.GetEpisodes
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesIntent
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesState
import dev.pimentel.rickandmorty.presentation.episodes.filter.EpisodesFilterFragment
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter
import dev.pimentel.rickandmorty.presentation.episodes.mappers.EpisodesItemMapper
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
class EpisodesViewModel @ViewModelInject constructor(
    private val getEpisodes: GetEpisodes,
    private val episodesItemMapper: EpisodesItemMapper,
    private val getErrorMessage: GetErrorMessage,
    private val navigator: Navigator,
    dispatchersProvider: DispatchersProvider
) : ViewModel(), ReactiveViewModel<EpisodesIntent, EpisodesState>,
    PagingHelper<Episode> by PagingHelperImpl(),
    Reducer<EpisodesState> by ReducerImpl(EpisodesState()) {

    private var lastFilter = EpisodesFilter.NO_FILTER

    override val intentChannel: Channel<EpisodesIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            intentChannel.consumeAsFlow().throttleFirst().collect { intent ->
                when (intent) {
                    is EpisodesIntent.GetEpisodes -> getEpisodes(intent.filter)
                    is EpisodesIntent.GetEpisodesWithLastFilter -> getEpisodes(lastFilter)
                    is EpisodesIntent.OpenFilters -> openFilters()
                }
            }
        }
    }

    override fun state(): StateFlow<EpisodesState> = mutableState

    private suspend fun getEpisodes(filter: EpisodesFilter) {
        handleFilterIconChange(filter)

        val reset = lastFilter != filter
        if (reset) {
            this.lastFilter = filter
            updateState { copy(scrollToTheTop = Unit, episodes = emptyList()) }
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
                val episodes = episodesItemMapper.getAll(response)
                updateState { copy(episodes = episodes) }
            },
            onError = { error ->
                val errorMessage = getErrorMessage(GetErrorMessage.Params(error))
                updateState { copy(errorMessage = errorMessage) }
            }
        )
    }

    private suspend fun handleFilterIconChange(filter: EpisodesFilter) {
        updateState {
            copy(
                filterIcon = if (filter != EpisodesFilter.NO_FILTER) R.drawable.ic_filter_selected
                else R.drawable.ic_filter_default
            )
        }
    }

    private fun openFilters() {
        navigator.navigate(
            R.id.episodes_to_episodes_filter,
            EpisodesFilterFragment.EPISODES_FILTER_ARGUMENT_KEY to lastFilter
        )
    }
}
