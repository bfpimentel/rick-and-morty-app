package dev.pimentel.rickandmorty.presentation.characters

import dev.pimentel.domain.usecases.GetCharacterDetails
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemMapper
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.testshared.ViewModelTest
import io.mockk.mockk

class CharactersViewModelTest : ViewModelTest<CharactersContract.ViewModel>() {

    private val getCharacters = mockk<GetCharacters>()
    private val getCharacterDetails = mockk<GetCharacterDetails>()
    private val charactersItemMapper = mockk<CharactersItemMapper>()
    private val characterDetailsMapper = mockk<CharacterDetailsMapper>()
    private val navigator = mockk<Navigator>()
    override lateinit var viewModel: CharactersContract.ViewModel

    override fun `setup subject`() {
        viewModel = CharactersViewModel(
            getCharacters,
            getCharacterDetails,
            charactersItemMapper,
            characterDetailsMapper,
            getErrorMessage,
            navigator,
            schedulerProvider
        )
    }
}
