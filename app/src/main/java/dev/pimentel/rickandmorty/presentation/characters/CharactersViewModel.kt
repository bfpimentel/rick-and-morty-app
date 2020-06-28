package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.entities.Character
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemMapper
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.navigator.NavigatorRouter
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import timber.log.Timber

class CharactersViewModel(
    private val getCharacters: GetCharacters,
    private val itemMapper: CharactersItemMapper,
    private val navigator: NavigatorRouter,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    CharactersContract.ViewModel {

    private var page: Int = DEFAULT_PAGE
    private var lastPage: Int = DEFAULT_LAST_PAGE
    private var lastFilter = CharactersFilter.NO_FILTER

    // going to be needed later
    private var characters: MutableList<Character> = mutableListOf()

    private val charactersState = MutableLiveData<CharactersState>()
    private val filterIcon = MutableLiveData<Int>()

    override fun charactersState(): LiveData<CharactersState> = charactersState
    override fun filterIcon(): LiveData<Int> = filterIcon

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    override fun getCharacters(filter: CharactersFilter) {
        if (lastFilter != filter) {
            page = FIRST_PAGE
            lastPage = DEFAULT_LAST_PAGE
            lastFilter = filter

            this.characters = mutableListOf()
            charactersState.postValue(CharactersState.Empty())
        } else {
            if (page == lastPage) {
                return
            }

            page++
        }

        handleFilterIconChange()

        getCharacters(
            GetCharacters.Params(
                page,
                filter.name,
                filter.species,
                filter.status,
                filter.gender
            )
        ).compose(observeOnUIAfterSingleResult())
            .handle({ response ->
                this.characters.addAll(response.characters)
                this.lastPage = response.pages

                charactersState.postValue(
                    CharactersState.Success(
                        this.characters.map(itemMapper::get)
                    )
                )
            }, Timber::e)
    }

    override fun getMoreCharacters() {
        getCharacters(lastFilter)
    }

    override fun openFilters() {
        navigator.navigate(
            R.id.characters_to_characters_filter,
            CharactersFilter.FILTER_ARGUMENT_KEY to lastFilter
        )
    }

    private fun handleFilterIconChange() {
        filterIcon.postValue(
            if (lastFilter != CharactersFilter.NO_FILTER) R.drawable.ic_filter_selected
            else R.drawable.ic_filter_default
        )
    }

    private companion object {
        const val DEFAULT_PAGE = 0
        const val FIRST_PAGE = 1
        const val DEFAULT_LAST_PAGE = -1
    }
}
