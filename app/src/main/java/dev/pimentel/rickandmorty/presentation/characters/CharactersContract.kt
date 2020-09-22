package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

interface CharactersContract {

    @ExperimentalCoroutinesApi
    interface ViewModel {
        fun getCharacters(filter: CharactersFilter)
        fun getCharactersWithLastFilter()
        fun openFilters()
        fun getDetails(id: Int)

        fun charactersState(): StateFlow<CharactersState>
        fun filterIcon(): LiveData<Int>
        fun error(): LiveData<String>
    }
}
