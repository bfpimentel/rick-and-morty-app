package dev.pimentel.rickandmorty.presentation.characters

import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDisplayMapperImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val charactersModule = module {
    viewModel {
        CharactersViewModel(
            get(),
            CharacterDisplayMapperImpl(),
            get()
        )
    }
    factory { CharactersAdapter() }
}
