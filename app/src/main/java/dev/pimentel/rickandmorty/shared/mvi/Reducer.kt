package dev.pimentel.rickandmorty.shared.mvi

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
interface Reducer<T> {
    val mutableState: MutableStateFlow<T>
    fun updateState(handler: T.() -> T)
}

@ExperimentalCoroutinesApi
class ReducerImpl<T>(
    initialState: T
) : Reducer<T> {

    override val mutableState = MutableStateFlow(initialState)

    override fun updateState(handler: T.() -> T) {
        mutableState.value = handler(mutableState.value)
    }
}
