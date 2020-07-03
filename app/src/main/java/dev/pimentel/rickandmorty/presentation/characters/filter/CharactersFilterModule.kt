package dev.pimentel.rickandmorty.presentation.characters.filter

import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val charactersFilterModule = module {
    viewModel { CharactersFilterViewModel(get<Navigator>()) }
}
