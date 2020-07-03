package dev.pimentel.rickandmorty.presentation.characters

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapperImpl
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapperImpl

@Module
@InstallIn(FragmentComponent::class)
object CharactersModule {

    @Provides
    @FragmentScoped
    fun providesCharactersAdapter(): CharactersAdapter = CharactersAdapter()

    @Provides
    @FragmentScoped
    fun providesCharacterDetailsMapper(
        @ApplicationContext context: Context
    ): CharacterDetailsMapper = CharacterDetailsMapperImpl(context)

    @Provides
    @FragmentScoped
    fun providesCharactersItemsMapper(): CharactersItemsMapper = CharactersItemsMapperImpl()
}
