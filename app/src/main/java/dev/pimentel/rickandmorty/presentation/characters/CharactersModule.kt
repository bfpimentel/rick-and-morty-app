package dev.pimentel.rickandmorty.presentation.characters

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.pimentel.domain.usecases.GetCharacterDetails
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapperImpl
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapperImpl
import dev.pimentel.rickandmorty.shared.dispatchersprovider.DispatchersProvider
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CharactersModule {

    @Provides
    @Singleton
    fun providesCharacterDetailsMapper(
        @ApplicationContext context: Context
    ): CharacterDetailsMapper = CharacterDetailsMapperImpl(context)

    @Provides
    @Singleton
    fun providesCharactersItemsMapper(): CharactersItemsMapper = CharactersItemsMapperImpl()

    @Provides
    fun provideCharactersStore(
        getCharacters: GetCharacters,
        getCharacterDetails: GetCharacterDetails,
        charactersItemsMapper: CharactersItemsMapper,
        characterDetailsMapper: CharacterDetailsMapper,
        getErrorMessage: GetErrorMessage,
        navigator: Navigator,
        dispatchersProvider: DispatchersProvider,
    ): CharactersStore = CharactersStore(
        getCharacters = getCharacters,
        getCharacterDetails = getCharacterDetails,
        charactersItemsMapper = charactersItemsMapper,
        characterDetailsMapper = characterDetailsMapper,
        getErrorMessage = getErrorMessage,
        navigator = navigator,
        dispatchersProvider = dispatchersProvider
    )
}
