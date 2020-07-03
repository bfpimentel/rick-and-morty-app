package dev.pimentel.rickandmorty.presentation.characters.details

import dev.pimentel.rickandmorty.presentation.characters.details.dto.CharacterDetails
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.testshared.ViewModelTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CharactersDetailsViewModelTest : ViewModelTest<CharactersDetailsContract.ViewModel>() {

    private val navigator = mockk<Navigator>()
    override lateinit var viewModel: CharactersDetailsContract.ViewModel

    override fun `setup subject`() {
        viewModel = CharactersDetailsViewModel(navigator)
    }

    @Test
    fun `should initialize and post details`() {
        val details = CharacterDetails(
            1,
            "name",
            "status",
            "species",
            "image",
            "gender",
            "origin",
            "type",
            "location",
            listOf(
                EpisodesItem(1, "name1", "airDate1", "number1"),
                EpisodesItem(2, "name2", "airDate2", "number2")
            )
        )

        viewModel.initialize(details)

        assertEquals(viewModel.characterDetails().value, details)

        confirmVerified(navigator)
    }

    @Test
    fun `should call navigator when closing`() {
        every { navigator.pop() } just runs

        viewModel.close()

        verify(exactly = 1) { navigator.pop() }
        confirmVerified(navigator)
    }
}
