package dev.pimentel.rickandmorty.presentation.locations

import dev.pimentel.rickandmorty.shared.navigator.Navigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationsModule = module {
    viewModel {
        LocationsViewModel(
            get<Navigator>(),
            get()
        )
    }
    factory { LocationsAdapter() }
}
