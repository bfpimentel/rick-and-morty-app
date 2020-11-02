package dev.pimentel.rickandmorty.shared.mvi

import dev.pimentel.rickandmorty.shared.dispatchersprovider.DispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

abstract class Store<Intent, State>(
    dispatchersProvider: DispatchersProvider,
    initialState: State,
) {

    private val mutableState = MutableStateFlow(initialState)
    private val intentChannel: Channel<Intent> = Channel(Channel.UNLIMITED)

    init {
        CoroutineScope(dispatchersProvider.io + Job()).launch {
            handleIntentions(intentChannel.consumeAsFlow())
        }
    }

    abstract suspend fun handleIntentions(intentions: Flow<Intent>)

    fun state(): StateFlow<State> = mutableState

    operator fun plusAssign(intent: Intent) {
        intentChannel.offer(intent)
    }

    protected suspend fun updateState(block: suspend State.() -> State) {
        mutableState.value = block(mutableState.value)
    }
}
