package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.characters.data.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.data.CharactersState

interface CharactersContract {

    interface ViewModel {
        fun getCharacters(filter: CharactersFilter)

        fun charactersState(): LiveData<CharactersState>
    }
}
