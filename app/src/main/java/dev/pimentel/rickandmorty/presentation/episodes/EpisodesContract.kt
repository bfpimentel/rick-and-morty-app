package dev.pimentel.rickandmorty.presentation.episodes

import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesIntent
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesState
import dev.pimentel.rickandmorty.shared.mvi.ReactiveViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
interface EpisodesContract {

    interface ViewModel : ReactiveViewModel<EpisodesIntent, EpisodesState>
}
