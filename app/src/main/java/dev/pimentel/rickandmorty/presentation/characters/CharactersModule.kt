package dev.pimentel.rickandmorty.presentation.characters

import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDisplayMapperImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val charactersModule = module {
    viewModel {
        CharactersViewModel(
            get(),
            CharacterDisplayMapperImpl(),
            get<Navigator>(),
            get()
        )
    }
    factory { CharactersAdapter() }
}
