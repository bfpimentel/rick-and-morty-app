package dev.pimentel.rickandmorty.presentation.characters.details

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.characters.details.dto.CharacterDetails

interface CharactersDetailsContract {

    interface ViewModel {
        fun initialize(characterDetails: CharacterDetails)
        fun close()

        fun characterDetails(): LiveData<CharacterDetails>
    }
}
