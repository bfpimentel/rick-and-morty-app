package dev.pimentel.rickandmorty.presentation.characters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pimentel.domain.entities.Character
import dev.pimentel.domain.usecases.GetCharacterDetails
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.characters.details.CharactersDetailsFragment
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersIntent
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.filter.CharactersFilterFragment
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapper
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.extensions.throttleFirst
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.helpers.PagingHelper
import dev.pimentel.rickandmorty.shared.helpers.PagingHelperImpl
import dev.pimentel.rickandmorty.shared.mvi.Reducer
import dev.pimentel.rickandmorty.shared.mvi.ReducerImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
@Suppress("LongParameterList")
class CharactersViewModel @ViewModelInject constructor(
    private val getCharacters: GetCharacters,
    private val getCharacterDetails: GetCharacterDetails,
    private val charactersItemsMapper: CharactersItemsMapper,
    private val characterDetailsMapper: CharacterDetailsMapper,
    private val getErrorMessage: GetErrorMessage,
    private val navigator: Navigator,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    PagingHelper<Character> by PagingHelperImpl(schedulerProvider),
    Reducer<CharactersState> by ReducerImpl(CharactersState()),
    CharactersContract.ViewModel {

    private var lastFilter = CharactersFilter.NO_FILTER

    override val intentChannel: Channel<CharactersIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().throttleFirst(1000L).collect { intent ->
                when (intent) {
                    is CharactersIntent.GetCharacters -> getCharacters(intent.filter)
                    is CharactersIntent.GetCharactersWithLastFilter -> getCharacters(lastFilter)
                    is CharactersIntent.OpenFilters -> openFilters()
                    is CharactersIntent.GetDetails -> getDetails(intent.characterId)
                }
            }
        }
    }

    override fun state(): StateFlow<CharactersState> = mutableState

    override fun onCleared() {
        super.onCleared()
        disposeHolder()
        disposePaging()
    }

    private fun getCharacters(filter: CharactersFilter) {
        handleFilterIconChange(filter)

        val reset = lastFilter != filter
        if (reset) {
            this.lastFilter = filter
            updateState { copy(characters = emptyList()) }
        }

        getCharacters(
            GetCharacters.Params(
                getCurrentPage(reset),
                filter.name,
                filter.species,
                filter.status,
                filter.gender
            )
        ).handlePaging(
            { result ->
                updateState {
                    copy(characters = charactersItemsMapper.getAll(result))
                }
            },
            { throwable ->
                updateState {
                    copy(listErrorMessage = getErrorMessage(GetErrorMessage.Params(throwable)))
                }
            }
        )
    }

    private fun openFilters() {
        navigator.navigate(
            R.id.characters_to_characters_filter,
            CharactersFilterFragment.CHARACTERS_FILTER_ARGUMENT_KEY to lastFilter
        )
    }

    private fun getDetails(id: Int) {
        getCharacterDetails(GetCharacterDetails.Params(id))
            .compose(observeOnUIAfterSingleResult())
            .handle({ response ->
                val details = characterDetailsMapper.get(response)

                navigator.navigate(
                    R.id.characters_to_characters_details,
                    CharactersDetailsFragment.CHARACTERS_DETAILS_ARGUMENT_KEY to details
                )
            }, { throwable ->
                val errorMessage = getErrorMessage(GetErrorMessage.Params(throwable))
                updateState {
                    copy(detailsErrorMessage = errorMessage)
                }
            })
    }

    private fun handleFilterIconChange(filter: CharactersFilter) {
        updateState {
            copy(
                filterIcon = if (filter != CharactersFilter.NO_FILTER) R.drawable.ic_filter_selected
                else R.drawable.ic_filter_default
            )
        }
    }
}
