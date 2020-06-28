package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersItem
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter

interface CharactersContract {

    interface ViewModel {
        fun getCharacters(filter: CharactersFilter)
        fun getMoreCharacters()
        fun openFilters()

        fun characters(): LiveData<List<CharactersItem>>
        fun filterIcon(): LiveData<Int>
    }
}
