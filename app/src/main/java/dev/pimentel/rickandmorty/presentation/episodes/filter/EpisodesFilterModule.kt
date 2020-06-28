package dev.pimentel.rickandmorty.presentation.episodes.filter

import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val episodesFilterModule = module {
    viewModel { EpisodesFilterViewModel(get<Navigator>()) }
}
