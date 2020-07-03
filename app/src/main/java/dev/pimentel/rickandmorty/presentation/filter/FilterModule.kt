package dev.pimentel.rickandmorty.presentation.filter

import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapperImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val filterModule = module {
    viewModel {
        FilterViewModel(
            FilterTypeMapperImpl(),
            get(),
            get(),
            get<Navigator>(),
            get()
        )
    }
}
