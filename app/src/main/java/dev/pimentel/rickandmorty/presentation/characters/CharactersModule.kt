package dev.pimentel.rickandmorty.presentation.characters

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object CharactersModule {

//    @Provides
//    @FragmentScoped
//    fun providesCharacterDetailsMapper(
//        @ApplicationContext context: Context
//    ): CharacterDetailsMapper = CharacterDetailsMapperImpl(context)
//
//    @Provides
//    @FragmentScoped
//    fun providesCharactersItemsMapper(): CharactersItemsMapper = CharactersItemsMapperImpl()
}
