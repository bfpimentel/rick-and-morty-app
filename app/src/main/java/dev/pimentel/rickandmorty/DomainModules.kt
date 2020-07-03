package dev.pimentel.rickandmorty

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.pimentel.domain.repositories.CharactersRepository
import dev.pimentel.domain.repositories.EpisodesRepository
import dev.pimentel.domain.repositories.FilterRepository
import dev.pimentel.domain.repositories.LocationsRepository
import dev.pimentel.domain.usecases.GetCharacterDetails
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.domain.usecases.GetEpisodes
import dev.pimentel.domain.usecases.GetFilters
import dev.pimentel.domain.usecases.GetLocations
import dev.pimentel.domain.usecases.SaveFilter
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DomainModules {

    @Provides
    @Singleton
    fun providesGetCharacters(
        charactersRepository: CharactersRepository
    ): GetCharacters = GetCharacters(charactersRepository)

    @Provides
    @Singleton
    fun providesGetLocations(
        locationsRepository: LocationsRepository
    ): GetLocations = GetLocations(locationsRepository)

    @Provides
    @Singleton
    fun providesGetEpisodes(
        episodesRepository: EpisodesRepository
    ): GetEpisodes = GetEpisodes(episodesRepository)

    @Provides
    @Singleton
    fun providesGetFilters(
        filterRepository: FilterRepository
    ): GetFilters = GetFilters(filterRepository)

    @Provides
    @Singleton
    fun providesSaveFilter(
        filterRepository: FilterRepository
    ): SaveFilter = SaveFilter(filterRepository)

    @Provides
    @Singleton
    fun providesGetCharacterDetails(
        charactersRepository: CharactersRepository
    ): GetCharacterDetails = GetCharacterDetails(charactersRepository)
}
