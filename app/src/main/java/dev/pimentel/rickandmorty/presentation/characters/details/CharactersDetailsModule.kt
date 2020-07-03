package dev.pimentel.rickandmorty.presentation.characters.details

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object CharactersDetailsModule {

    @Provides
    @FragmentScoped
    fun providesCharactersDetailsEpisodesAdapter(): CharactersDetailsEpisodesAdapter =
        CharactersDetailsEpisodesAdapter()
}
