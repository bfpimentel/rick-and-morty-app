package dev.pimentel.rickandmorty.presentation.characters

import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapperImpl
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemMapperImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val charactersModule = module {
    viewModel {
        CharactersViewModel(
            get(),
            get(),
            CharactersItemMapperImpl(),
            CharacterDetailsMapperImpl(androidContext()),
            get(),
            get<Navigator>(),
            get()
        )
    }
    factory { CharactersAdapter() }
}
