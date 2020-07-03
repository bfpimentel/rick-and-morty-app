package dev.pimentel.rickandmorty.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapperImpl
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapperImpl
import dev.pimentel.rickandmorty.presentation.episodes.mappers.EpisodesItemMapper
import dev.pimentel.rickandmorty.presentation.episodes.mappers.EpisodesItemMapperImpl
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapper
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapperImpl
import dev.pimentel.rickandmorty.presentation.locations.mappers.LocationsItemMapper
import dev.pimentel.rickandmorty.presentation.locations.mappers.LocationsItemMapperImpl
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.shared.navigator.NavigatorImpl
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

    @Provides
    @Singleton
    fun provideNavigator(): Navigator = NavigatorImpl()

    @Provides
    @Singleton
    fun provideGetErrorMessage(
        @ApplicationContext context: Context
    ): GetErrorMessage = GetErrorMessage(context)

    /* region [SCOPED DEPENDENCIES] TODO: Inject only when each ViewModel is created*/
//    @Provides
//    @Singleton
//    fun providesCharacterDetailsMapper(
//        @ApplicationContext context: Context
//    ): CharacterDetailsMapper = CharacterDetailsMapperImpl(context)
//
//    @Provides
//    @Singleton
//    fun providesCharactersItemsMapper(): CharactersItemsMapper = CharactersItemsMapperImpl()

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
