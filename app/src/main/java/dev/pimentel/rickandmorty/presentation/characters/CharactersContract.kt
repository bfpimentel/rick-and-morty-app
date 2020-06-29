package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter

interface CharactersContract {

    interface ViewModel {
        fun getCharacters(filter: CharactersFilter)
        fun getCharactersWithLastFilter()
        fun openFilters()
        fun getDetails(id: Int)

        fun charactersState(): LiveData<CharactersState>
        fun filterIcon(): LiveData<Int>
    }
}
