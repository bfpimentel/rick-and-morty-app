package dev.pimentel.rickandmorty.presentation.characters

import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersIntent
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
interface CharactersContract {

    interface ViewModel {
        val intentChannel: Channel<CharactersIntent>

        fun charactersState(): StateFlow<CharactersState>
        fun filterIcon(): StateFlow<Int>
        fun error(): StateFlow<String?>
    }
}
