package dev.pimentel.rickandmorty.presentation.characters

import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemMapperImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val charactersModule = module {
    viewModel {
        CharactersViewModel(
            get(),
            get(),
            CharactersItemMapperImpl(),
            get<Navigator>(),
            get()
        )
    }
    factory { CharactersAdapter() }
}
