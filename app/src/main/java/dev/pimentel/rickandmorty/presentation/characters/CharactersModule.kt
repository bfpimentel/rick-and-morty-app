package dev.pimentel.rickandmorty.presentation.characters

import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapperImpl
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapperImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val charactersModule = module {
    viewModel {
        CharactersViewModel(
            get(),
            get(),
            CharactersItemsMapperImpl(),
            CharacterDetailsMapperImpl(androidContext()),
            get(),
            get<Navigator>(),
            get()
        )
    }
    factory { CharactersAdapter() }
}
