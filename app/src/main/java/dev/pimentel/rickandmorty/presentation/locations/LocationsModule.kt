package dev.pimentel.rickandmorty.presentation.locations

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object LocationsModule {

    @Provides
    @FragmentScoped
    fun providesLocationsAdapter(): LocationsAdapter = LocationsAdapter()

//    @Provides
//    @FragmentScoped
//    fun providesLocationsItemMapper(): LocationsItemMapper = LocationsItemMapperImpl()
}
