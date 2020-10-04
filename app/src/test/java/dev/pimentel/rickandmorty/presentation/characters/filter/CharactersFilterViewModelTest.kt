package dev.pimentel.rickandmorty.presentation.characters.filter

import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilterState
import dev.pimentel.rickandmorty.presentation.filter.FilterDialog
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.testshared.ViewModelTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CharactersFilterViewModelTest : ViewModelTest<CharactersFilterContract.ViewModel>() {

    private val navigator = mockk<Navigator>()
    override lateinit var viewModel: CharactersFilterContract.ViewModel

    override fun `setup subject`() {
        viewModel = CharactersFilterViewModel(navigator)
    }

    @Test
    @BeforeEach
    fun `should initialize with filter and post the first filter state`() {
        val filter = CharactersFilter(null, null, null, null)
        val state = CharactersFilterState(
            canApplyFilter = false,
            canClear = false,
            name = null,
            species = null,
            selectedStatusIndex = null,
            selectedGenderIndex = null
        )

        viewModel.initializeWithFilter(filter)

        assertEquals(viewModel.charactersFilterState().value, state)
    }

    @Test
    fun `should set name and post filter state with it`() {
        val name = "name"
        val state = CharactersFilterState(
            canApplyFilter = true,
            canClear = true,
            name = name,
            species = null,
            selectedStatusIndex = null,
            selectedGenderIndex = null
        )

        viewModel.setTextFilter(FilterResult(FilterType.CHARACTER_NAME, name))

        assertEquals(viewModel.charactersFilterState().value, state)

        confirmVerified(navigator)
    }

    @Test
    fun `should set species and post filter state with it`() {
        val species = "species"
        val state = CharactersFilterState(
            canApplyFilter = true,
            canClear = true,
            name = null,
            species = species,
            selectedStatusIndex = null,
            selectedGenderIndex = null
        )

        viewModel.setTextFilter(FilterResult(FilterType.CHARACTER_SPECIES, species))

        assertEquals(viewModel.charactersFilterState().value, state)

        confirmVerified(navigator)
    }

    @Test
    fun `should set status and post filter state with it`() {
        val status = R.id.status_alive
        val state = CharactersFilterState(
            canApplyFilter = true,
            canClear = true,
            name = null,
            species = null,
            selectedStatusIndex = status,
            selectedGenderIndex = null
        )

        viewModel.setStatus(status)

        assertEquals(viewModel.charactersFilterState().value, state)

        confirmVerified(navigator)
    }

    @Test
    fun `should set status as null when id does not exist and post filter state with it`() {
        val status = -1
        val state = CharactersFilterState(
            canApplyFilter = false,
            canClear = false,
            name = null,
            species = null,
            selectedStatusIndex = null,
            selectedGenderIndex = null
        )

        viewModel.setStatus(status)

        assertEquals(viewModel.charactersFilterState().value, state)

        confirmVerified(navigator)
    }

    @Test
    fun `should set gender and post filter state with it`() {
        val gender = R.id.gender_female
        val state = CharactersFilterState(
            canApplyFilter = true,
            canClear = true,
            name = null,
            species = null,
            selectedStatusIndex = null,
            selectedGenderIndex = gender
        )

        viewModel.setGender(gender)

        assertEquals(viewModel.charactersFilterState().value, state)

        confirmVerified(navigator)
    }

    @Test
    fun `should set gender as null when id does not exist and post filter state with it`() {
        val gender = -1
        val state = CharactersFilterState(
            canApplyFilter = false,
            canClear = false,
            name = null,
            species = null,
            selectedStatusIndex = null,
            selectedGenderIndex = null
        )

        viewModel.setGender(gender)

        assertEquals(viewModel.charactersFilterState().value, state)

        confirmVerified(navigator)
    }

    @Test
    fun `should clear filter`() {
        val state = CharactersFilterState(
            canApplyFilter = false,
            canClear = false,
            name = null,
            species = null,
            selectedStatusIndex = null,
            selectedGenderIndex = null
        )

        viewModel.clearFilter()

        assertEquals(viewModel.charactersFilterState().value, state)

        confirmVerified(navigator)
    }

    @Test
    fun `should post current filter on filtering result when getting filter`() {
        val result = CharactersFilter(
            "name",
            null,
            "alive",
            "female"
        )

        every { navigator.pop() } just runs

        viewModel.setTextFilter(FilterResult(FilterType.CHARACTER_NAME, "name"))
        viewModel.setStatus(R.id.status_alive)
        viewModel.setGender(R.id.gender_female)

        viewModel.getFilter()

        assertEquals(viewModel.filteringResult().value, result)

        verify(exactly = 1) { navigator.pop() }
        confirmVerified(navigator)
    }

    @Test
    fun `should open name filter`() {
        every {
            navigator.navigate(
                R.id.characters_filter_to_filter,
                FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.CHARACTER_NAME
            )
        } just runs

        viewModel.openNameFilter()

        verify(exactly = 1) {
            navigator.navigate(
                R.id.characters_filter_to_filter,
                FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.CHARACTER_NAME
            )
        }
        confirmVerified(navigator)
    }

    @Test
    fun `should open species filter`() {
        every {
            navigator.navigate(
                R.id.characters_filter_to_filter,
                FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.CHARACTER_SPECIES
            )
        } just runs

        viewModel.openSpeciesFilter()

        verify(exactly = 1) {
            navigator.navigate(
                R.id.characters_filter_to_filter,
                FilterDialog.FILTER_TYPE_ARGUMENT_KEY to FilterType.CHARACTER_SPECIES
            )
        }
        confirmVerified(navigator)
    }
}
