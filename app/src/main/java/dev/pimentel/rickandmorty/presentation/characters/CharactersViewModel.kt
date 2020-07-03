package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.entities.Character
import dev.pimentel.domain.usecases.GetCharacterDetails
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.characters.details.CharactersDetailsFragment
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.filter.CharactersFilterFragment
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapper
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.helpers.PagingHelper
import dev.pimentel.rickandmorty.shared.helpers.PagingHelperImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider

@Suppress("LongParameterList")
class CharactersViewModel(
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
    CharactersContract.ViewModel {

    private var lastFilter = CharactersFilter.NO_FILTER

    private val charactersState = MutableLiveData<CharactersState>()
    private val filterIcon = MutableLiveData<Int>()
    private val error = MutableLiveData<String>()

    override fun charactersState(): LiveData<CharactersState> = charactersState
    override fun filterIcon(): LiveData<Int> = filterIcon
    override fun error(): LiveData<String> = error

    override fun onCleared() {
        super.onCleared()
        disposeHolder()
        disposePaging()
    }

    override fun getCharacters(filter: CharactersFilter) {
        handleFilterIconChange(filter)

        val reset = lastFilter != filter
        if (reset) {
            this.lastFilter = filter
            charactersState.postValue(CharactersState.Empty())
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
                charactersState.postValue(
                    CharactersState.Success(charactersItemsMapper.getAll(result))
                )
            },
            { throwable ->
                charactersState.postValue(
                    CharactersState.Error(getErrorMessage(GetErrorMessage.Params(throwable)))
                )
            }
        )
    }

    override fun getCharactersWithLastFilter() {
        getCharacters(lastFilter)
    }

    override fun openFilters() {
        navigator.navigate(
            R.id.characters_to_characters_filter,
            CharactersFilterFragment.CHARACTERS_FILTER_ARGUMENT_KEY to lastFilter
        )
    }

    override fun getDetails(id: Int) {
        getCharacterDetails(GetCharacterDetails.Params(id))
            .compose(observeOnUIAfterSingleResult())
            .handle({ response ->
                val details = characterDetailsMapper.get(response)

                navigator.navigate(
                    R.id.characters_to_characters_details,
                    CharactersDetailsFragment.CHARACTERS_DETAILS_ARGUMENT_KEY to details
                )
            }, { throwable ->
                error.postValue(
                    getErrorMessage(GetErrorMessage.Params(throwable))
                )
            })
    }

    private fun handleFilterIconChange(filter: CharactersFilter) {
        filterIcon.postValue(
            if (filter != CharactersFilter.NO_FILTER) R.drawable.ic_filter_selected
            else R.drawable.ic_filter_default
        )
    }
}
