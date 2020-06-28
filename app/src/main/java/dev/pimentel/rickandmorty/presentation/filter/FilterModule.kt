package dev.pimentel.rickandmorty.presentation.filter

import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val filterModule = module {
    viewModel { FilterViewModel(
        get<Navigator>()
    ) }
}
