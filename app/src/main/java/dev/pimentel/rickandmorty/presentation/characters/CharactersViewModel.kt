package dev.pimentel.rickandmorty.presentation.characters

import android.util.Log
import androidx.lifecycle.ViewModel
import dev.pimentel.data.sources.CharactersDataSource
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider

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
            .handle({}, { Log.e("teste", "", it) })
    }
}
