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
import dev.pimentel.rickandmorty.shared.dispatchersprovider.DispatchersProvider
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.extensions.throttleFirst
import dev.pimentel.rickandmorty.shared.helpers.PagingHelper
import dev.pimentel.rickandmorty.shared.helpers.PagingHelperImpl
import dev.pimentel.rickandmorty.shared.mvi.Reducer
import dev.pimentel.rickandmorty.shared.mvi.ReducerImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
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
    dispatchersProvider: DispatchersProvider
) : ViewModel(),
    Reducer<CharactersState> by ReducerImpl(CharactersState()),
    PagingHelper<Character> by PagingHelperImpl(),
    CharactersContract.ViewModel {

    private var lastFilter = CharactersFilter.NO_FILTER

    override val intentChannel: Channel<CharactersIntent> = Channel(Channel.UNLIMITED)

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            intentChannel.consumeAsFlow().throttleFirst(1000L).collect { intent ->
                when (intent) {
                    is CharactersIntent.GetCharacters -> getCharacters(intent.filter)
                    is CharactersIntent.GetCharactersWithLastFilter -> getCharacters(lastFilter)
                    is CharactersIntent.GetDetails -> getDetails(intent.characterId)
                    is CharactersIntent.OpenFilters -> openFilters()
                }
            }
        }
    }

    override fun state(): StateFlow<CharactersState> = mutableState

    private suspend fun getCharacters(filter: CharactersFilter) {
        handleFilterIconChange(filter)

        val reset = lastFilter != filter
        if (reset) {
            this.lastFilter = filter
            updateState { copy(characters = emptyList()) }
        }

        handlePaging(
            request = {
                getCharacters(
                    GetCharacters.Params(
                        getCurrentPage(reset),
                        filter.name,
                        filter.species,
                        filter.status,
                        filter.gender
                    )
                )
            },
            onSuccess = { result ->
                updateState {
                    copy(characters = charactersItemsMapper.getAll(result))
                }
            },
            onError = { exception ->
                val errorMessage = getErrorMessage(GetErrorMessage.Params(exception))
                updateState {
                    copy(listErrorMessage = errorMessage)
                }
            }
        )
    }

    private suspend fun getDetails(id: Int) {
        try {
            val result = getCharacterDetails(GetCharacterDetails.Params(id))
            val details = characterDetailsMapper.get(result)

            navigator.navigate(
                R.id.characters_to_characters_details,
                CharactersDetailsFragment.CHARACTERS_DETAILS_ARGUMENT_KEY to details
            )
        } catch (exception: Exception) {
            val errorMessage = getErrorMessage(GetErrorMessage.Params(exception))
            updateState {
                copy(detailsErrorMessage = errorMessage)
            }
        }
    }

    private fun openFilters() {
        navigator.navigate(
            R.id.characters_to_characters_filter,
            CharactersFilterFragment.CHARACTERS_FILTER_ARGUMENT_KEY to lastFilter
        )
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
