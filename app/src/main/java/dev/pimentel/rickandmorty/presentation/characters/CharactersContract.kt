package dev.pimentel.rickandmorty.presentation.characters

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.characters.data.CharactersState

interface CharactersContract {

    interface ViewModel {
        fun getCharacters()

        fun charactersState(): LiveData<CharactersState>
    }
}
