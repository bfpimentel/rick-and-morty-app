package dev.pimentel.rickandmorty.presentation.characters

import dev.pimentel.domain.entities.Character
import dev.pimentel.domain.entities.Episode
import dev.pimentel.domain.entities.Pageable
import dev.pimentel.domain.usecases.GetCharacterDetails
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.characters.details.CharactersDetailsFragment
import dev.pimentel.rickandmorty.presentation.characters.details.dto.CharacterDetails
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersItem
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.filter.CharactersFilterFragment
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharacterDetailsMapper
import dev.pimentel.rickandmorty.presentation.characters.mappers.CharactersItemsMapper
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.testshared.ViewModelTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import dev.pimentel.domain.entities.CharacterDetails as CharacterDetailsEntity

class CharactersViewModelTest : ViewModelTest<CharactersContract.ViewModel>() {

    private val getCharacters = mockk<GetCharacters>()
    private val getCharacterDetails = mockk<GetCharacterDetails>()
    private val charactersItemMapper = mockk<CharactersItemsMapper>()
    private val characterDetailsMapper = mockk<CharacterDetailsMapper>()
    private val navigator = mockk<Navigator>()
    override lateinit var viewModel: CharactersContract.ViewModel

    override fun `setup subject`() {
        viewModel = CharactersStore(
            getCharacters,
            getCharacterDetails,
            charactersItemMapper,
            characterDetailsMapper,
            getErrorMessage,
            navigator,
            schedulerProvider
        )
    }

    @Test
    fun `should get characters and post its result after success`() {
        val filter = CharactersFilter("name", "species", "status", "gender")

        val getCharactersParams = GetCharacters.Params(1, "name", "species", "status", "gender")
        val charactersResponse = listOf(
            Character(1, "name1", "status1", "image1"),
            Character(2, "name2", "status2", "image2")
        )
        val response = Pageable(20, charactersResponse)
        val mappedResponse = listOf(
            CharactersItem(1, "image1", "status1", "name1"),
            CharactersItem(2, "image2", "status2", "name2")
        )

        every { getCharacters(getCharactersParams) } returns Single.just(response)
        every { charactersItemMapper.getAll(charactersResponse) } returns mappedResponse

        viewModel.getCharacters(filter)

        assert(viewModel.charactersState().value is CharactersState.Empty)

        testScheduler.triggerActions()

        assert(viewModel.charactersState().value is CharactersState.Success)
        assertEquals(
            (viewModel.charactersState().value as CharactersState).characters,
            mappedResponse
        )

        assertEquals(viewModel.filterIcon().value, R.drawable.ic_filter_selected)

        verify(exactly = 1) {
            getCharacters(getCharactersParams)
            charactersItemMapper.getAll(charactersResponse)
        }
        confirmVerified(
            getCharacters,
            getCharacterDetails,
            charactersItemMapper,
            characterDetailsMapper,
            getErrorMessage,
            navigator
        )
    }

    @Test
    fun `should get characters and post its error message after error`() {
        val filter = CharactersFilter(null, null, null, null)
        val getCharactersParams = GetCharacters.Params(1, null, null, null, null)

        val error = IllegalArgumentException()
        val getErrorMessageParams = GetErrorMessage.Params(error)
        val errorMessage = "message"

        every { getCharacters(getCharactersParams) } returns Single.error(error)
        every { getErrorMessage(getErrorMessageParams) } returns errorMessage

        viewModel.getCharacters(filter)
        testScheduler.triggerActions()

        assert(viewModel.charactersState().value is CharactersState.Error)
        assertEquals(
            (viewModel.charactersState().value as CharactersState).listErrorMessage,
            errorMessage
        )

        assertEquals(viewModel.filterIcon().value, R.drawable.ic_filter_default)

        verify(exactly = 1) {
            getCharacters(getCharactersParams)
            getErrorMessage(getErrorMessageParams)
        }
        confirmVerified(
            getCharacters,
            getCharacterDetails,
            charactersItemMapper,
            characterDetailsMapper,
            getErrorMessage,
            navigator
        )
    }

    @Test
    fun `should get characters with last filter and post its result after success`() {
        val filter = CharactersFilter("name", "species", "status", "gender")

        val getCharactersParams1 = GetCharacters.Params(1, "name", "species", "status", "gender")
        val charactersResponse1 = listOf(
            Character(1, "name1", "status1", "image1"),
            Character(2, "name2", "status2", "image2")
        )
        val response1 = Pageable(20, charactersResponse1)
        val mappedResponse1 = listOf(
            CharactersItem(1, "image1", "status1", "name1"),
            CharactersItem(2, "image2", "status2", "name2")
        )

        val getCharactersParams2 = GetCharacters.Params(2, "name", "species", "status", "gender")
        val charactersResponse2 = listOf(
            Character(3, "name3", "status3", "image3"),
            Character(4, "name4", "status4", "image4")
        )
        val response2 = Pageable(20, charactersResponse2)
        val mappedResponse2 = listOf(
            CharactersItem(3, "image3", "status3", "name3"),
            CharactersItem(4, "image4", "status4", "name4")
        )

        every { getCharacters(getCharactersParams1) } returns Single.just(response1)
        every { getCharacters(getCharactersParams2) } returns Single.just(response2)
        every {
            charactersItemMapper.getAll(charactersResponse1)
        } returns mappedResponse1
        every {
            charactersItemMapper.getAll(charactersResponse1 + charactersResponse2)
        } returns mappedResponse1 + mappedResponse2

        viewModel.getCharacters(filter)
        testScheduler.triggerActions()

        viewModel.getCharactersWithLastFilter()
        testScheduler.triggerActions()

        assert(viewModel.charactersState().value is CharactersState.Success)
        assertEquals(
            (viewModel.charactersState().value as CharactersState).characters,
            mappedResponse1 + mappedResponse2
        )
    }

    @Test
    fun `should get characters with new filter and post its result after success`() {
        val filter1 = CharactersFilter("name1", "species1", "status1", "gender1")
        val getCharactersParams1 =
            GetCharacters.Params(1, "name1", "species1", "status1", "gender1")
        val charactersResponse1 = listOf(
            Character(1, "name1", "status1", "image1"),
            Character(2, "name2", "status2", "image2")
        )
        val response1 = Pageable(20, charactersResponse1)
        val mappedResponse1 = listOf(
            CharactersItem(1, "image1", "status1", "name1"),
            CharactersItem(2, "image2", "status2", "name2")
        )

        val filter2 = CharactersFilter("name2", "species2", "status2", "gender2")
        val getCharactersParams2 =
            GetCharacters.Params(1, "name2", "species2", "status2", "gender2")
        val charactersResponse2 = listOf(
            Character(3, "name3", "status3", "image3"),
            Character(4, "name4", "status4", "image4")
        )
        val response2 = Pageable(20, charactersResponse2)
        val mappedResponse2 = listOf(
            CharactersItem(3, "image3", "status3", "name3"),
            CharactersItem(4, "image4", "status4", "name4")
        )

        every { getCharacters(getCharactersParams1) } returns Single.just(response1)
        every { getCharacters(getCharactersParams2) } returns Single.just(response2)
        every { charactersItemMapper.getAll(charactersResponse1) } returns mappedResponse1
        every { charactersItemMapper.getAll(charactersResponse2) } returns mappedResponse2

        viewModel.getCharacters(filter1)
        testScheduler.triggerActions()

        viewModel.getCharacters(filter2)
        testScheduler.triggerActions()

        assert(viewModel.charactersState().value is CharactersState.Success)
        assertEquals(
            (viewModel.charactersState().value as CharactersState).characters,
            mappedResponse2
        )
    }

    @Test
    fun `should open filters`() {
        val filter = CharactersFilter("name", "species", "status", "gender")

        val getCharactersParams = GetCharacters.Params(1, "name", "species", "status", "gender")
        val charactersResponse = listOf(
            Character(1, "name1", "status1", "image1"),
            Character(2, "name2", "status2", "image2")
        )
        val response = Pageable(20, charactersResponse)
        val mappedResponse = listOf(
            CharactersItem(1, "image1", "status1", "name1"),
            CharactersItem(2, "image2", "status2", "name2")
        )

        every { getCharacters(getCharactersParams) } returns Single.just(response)
        every { charactersItemMapper.getAll(charactersResponse) } returns mappedResponse
        every {
            navigator.navigate(
                R.id.characters_to_characters_filter,
                CharactersFilterFragment.CHARACTERS_FILTER_ARGUMENT_KEY to filter
            )
        } just runs

        viewModel.getCharacters(filter)
        testScheduler.triggerActions()

        viewModel.openFilters()

        verify(exactly = 1) {
            getCharacters(getCharactersParams)
            charactersItemMapper.getAll(charactersResponse)
            navigator.navigate(
                R.id.characters_to_characters_filter,
                CharactersFilterFragment.CHARACTERS_FILTER_ARGUMENT_KEY to filter
            )
        }
        confirmVerified(
            getCharacters,
            getCharacterDetails,
            charactersItemMapper,
            characterDetailsMapper,
            getErrorMessage,
            navigator
        )
    }

    @Test
    fun `should navigate to character details after getting it successfully`() {
        val id = 1
        val getCharacterDetailsParams = GetCharacterDetails.Params(id)

        val response = CharacterDetailsEntity(
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
                Episode(1, "name1", "airDate1", "number1"),
                Episode(2, "name2", "airDate2", "number2")
            )
        )

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

        every { getCharacterDetails(getCharacterDetailsParams) } returns Single.just(response)
        every { characterDetailsMapper.get(response) } returns details
        every {
            navigator.navigate(
                R.id.characters_to_characters_details,
                CharactersDetailsFragment.CHARACTERS_DETAILS_ARGUMENT_KEY to details
            )
        } just runs

        viewModel.getDetails(id)
        testScheduler.triggerActions()

        verify(exactly = 1) {
            getCharacterDetails(getCharacterDetailsParams)
            characterDetailsMapper.get(response)
            navigator.navigate(
                R.id.characters_to_characters_details,
                CharactersDetailsFragment.CHARACTERS_DETAILS_ARGUMENT_KEY to details
            )
        }
        confirmVerified(
            getCharacters,
            getCharacterDetails,
            charactersItemMapper,
            characterDetailsMapper,
            getErrorMessage,
            navigator
        )
    }

    @Test
    fun `should post error when failing to get character details`() {
        val id = 1
        val getCharacterDetailsParams = GetCharacterDetails.Params(id)
        val error = IllegalArgumentException()
        val getErrorMessageParams = GetErrorMessage.Params(error)
        val errorMessage = "message"

        every { getCharacterDetails(getCharacterDetailsParams) } returns Single.error(error)
        every { getErrorMessage(getErrorMessageParams) } returns errorMessage

        viewModel.getDetails(id)
        testScheduler.triggerActions()

        assertEquals(viewModel.error().value, errorMessage)

        verify(exactly = 1) {
            getCharacterDetails(getCharacterDetailsParams)
            getErrorMessage(getErrorMessageParams)
        }
        confirmVerified(
            getCharacters,
            getCharacterDetails,
            charactersItemMapper,
            characterDetailsMapper,
            getErrorMessage,
            navigator
        )
    }
}
