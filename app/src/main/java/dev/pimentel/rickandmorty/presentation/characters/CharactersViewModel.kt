package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.ViewModel
import dev.pimentel.data.sources.CharactersDataSource
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import timber.log.Timber

class CharactersViewModel(
    private val getCharacters: CharactersDataSource,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    CharactersContract.ViewModel {

    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    override fun getCharacters() {
        getCharacters.getCharacters(0)
            .compose(observeOnUIAfterSingleResult())
            .handle({}, Timber::e)
    }
}
