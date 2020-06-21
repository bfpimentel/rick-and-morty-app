package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.domain.usecases.shared.NoParams
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

    private val charactersState = MutableLiveData<CharactersState>()

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    override fun getCharacters() {
        getCharacters(NoParams)
            .compose(observeOnUIAfterSingleResult())
            .handle({ characters ->
                charactersState.postValue(
                    CharactersState.Success(
                        characters.map(characterDisplayMapper::get)
                    )
                )
            }, Timber::e)
    }

    override fun charactersState(): LiveData<CharactersState> = charactersState
}
