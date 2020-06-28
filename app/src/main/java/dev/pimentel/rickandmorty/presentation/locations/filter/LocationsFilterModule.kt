package dev.pimentel.rickandmorty.presentation.locations.filter

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationsFilterModule = module {
    viewModel { LocationsFilterViewModel(get()) }
}
