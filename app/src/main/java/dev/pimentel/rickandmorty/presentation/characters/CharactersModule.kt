package dev.pimentel.rickandmorty.presentation.characters

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import dev.pimentel.domain.usecases.GetCharacterDetails
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapperImpl
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapperImpl
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider

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

    @Suppress("UNCHECKED_CAST")
    @Provides
    @FragmentScoped
    fun providesCharactersViewModel(
        getCharacters: GetCharacters,
        getCharacterDetails: GetCharacterDetails,
        charactersItemsMapper: CharactersItemsMapper,
        characterDetailsMapper: CharacterDetailsMapper,
        getErrorMessage: GetErrorMessage,
        navigator: Navigator,
        schedulerProvider: SchedulerProvider
    ): CharactersContract.ViewModel = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
                CharactersViewModel(
                    getCharacters,
                    getCharacterDetails,
                    charactersItemsMapper,
                    characterDetailsMapper,
                    getErrorMessage,
                    navigator,
                    schedulerProvider
                ) as T
            } else throw IllegalArgumentException()
        }
    }.create(CharactersViewModel::class.java)
}
