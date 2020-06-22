package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.entities.Character
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.rickandmorty.presentation.characters.data.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.data.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDisplayMapper
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import timber.log.Timber

class CharactersViewModel(
    private val getCharacters: GetCharacters,
    private val characterDisplayMapper: CharacterDisplayMapper,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    CharactersContract.ViewModel {

    private var page: Int = DEFAULT_PAGE
    private var lastPage: Int = DEFAULT_LAST_PAGE

    private var lastFilter = CharactersFilter.BLANK
    private var characters: MutableList<Character> = mutableListOf()
    private val charactersState = MutableLiveData<CharactersState>()

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    override fun getCharacters(filter: CharactersFilter) {
        if (filter != lastFilter) {
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

        getCharacters(
            GetCharacters.Params(
                page,
                filter.name
            )
        ).compose(observeOnUIAfterSingleResult())
            .handle({ response ->
                this.characters.addAll(response.characters)
                this.lastPage = response.pages

                charactersState.postValue(
                    CharactersState.Success(
                        this.characters.map(characterDisplayMapper::get)
                    )
                )
            }, Timber::e)
    }

    override fun getMoreCharacters() {
        getCharacters(lastFilter)
    }

    override fun charactersState(): LiveData<CharactersState> = charactersState

    private companion object {
        const val DEFAULT_PAGE = 0
        const val FIRST_PAGE = 1
        const val DEFAULT_LAST_PAGE = -1
    }
}
