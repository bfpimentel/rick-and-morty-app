package dev.pimentel.rickandmorty.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.pimentel.rickandmorty.presentation.episodes.mappers.EpisodesItemMapper
import dev.pimentel.rickandmorty.presentation.episodes.mappers.EpisodesItemMapperImpl
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapper
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapperImpl
import dev.pimentel.rickandmorty.presentation.locations.mappers.LocationsItemMapper
import dev.pimentel.rickandmorty.presentation.locations.mappers.LocationsItemMapperImpl
import dev.pimentel.rickandmorty.shared.dispatchersprovider.AppDispatchersProvider
import dev.pimentel.rickandmorty.shared.dispatchersprovider.DispatchersProvider
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.shared.navigator.NavigatorImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatchersProvider = AppDispatchersProvider()

    @Provides
    @Singleton
    fun provideNavigator(
        dispatchersProvider: DispatchersProvider
    ): Navigator = NavigatorImpl(
        dispatchersProvider = dispatchersProvider
    )

    @Provides
    @Singleton
    fun provideGetErrorMessage(
        @ApplicationContext context: Context
    ): GetErrorMessage = GetErrorMessage(context)

    /* region [SCOPED DEPENDENCIES] */
    @Provides
    @Singleton
    fun providesLocationsItemMapper(): LocationsItemMapper = LocationsItemMapperImpl()

    @Provides
    @Singleton
    fun providesEpisodesItemMapper(
        @ApplicationContext context: Context
    ): EpisodesItemMapper = EpisodesItemMapperImpl(context)

    @Provides
    @Singleton
    fun providesFilterTypeMapper(): FilterTypeMapper = FilterTypeMapperImpl()
    /* endregion [SCOPED DEPENDENCIES] */
}
