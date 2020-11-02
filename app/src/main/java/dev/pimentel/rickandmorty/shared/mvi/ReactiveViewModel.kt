package dev.pimentel.rickandmorty.shared.mvi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow

interface ReactiveViewModel<Intent, State> {
    val intentChannel: Channel<Intent>
    fun state(): StateFlow<State>
}
