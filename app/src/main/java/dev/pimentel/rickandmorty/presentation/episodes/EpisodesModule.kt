package dev.pimentel.rickandmorty.presentation.episodes

import dev.pimentel.rickandmorty.presentation.episodes.mappers.EpisodesItemMapperImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val episodesModule = module {
    viewModel {
        EpisodesViewModel(
            get(),
            EpisodesItemMapperImpl(androidContext()),
            get(),
            get<Navigator>(),
            get()
        )
    }
    factory { EpisodesAdapter() }
}
