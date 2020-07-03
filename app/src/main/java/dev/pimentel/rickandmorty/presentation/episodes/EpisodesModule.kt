package dev.pimentel.rickandmorty.presentation.episodes

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object EpisodesModule {

    @Provides
    @FragmentScoped
    fun providesEpisodesAdapter(): EpisodesAdapter = EpisodesAdapter()

//    @Provides
//    @FragmentScoped
//    fun providesEpisodesItemMapper(
//        @ApplicationContext context: Context
//    ): EpisodesItemMapper = EpisodesItemMapperImpl(context)
}
