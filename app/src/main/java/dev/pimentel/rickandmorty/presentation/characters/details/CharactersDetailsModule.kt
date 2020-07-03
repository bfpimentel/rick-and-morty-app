package dev.pimentel.rickandmorty.presentation.characters.details

import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val charactersDetailsModule = module {
    viewModel { CharactersDetailsViewModel(get<Navigator>()) }
    factory { CharactersDetailsEpisodesAdapter() }
}
