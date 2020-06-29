package dev.pimentel.rickandmorty.presentation.filter

import dev.pimentel.domain.models.FilterModel
import dev.pimentel.domain.usecases.GetFilters
import dev.pimentel.domain.usecases.SaveFilter
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterState
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapper
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.testshared.ViewModelTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FilterViewModelTest : ViewModelTest<FilterContract.ViewModel>() {

    private val filterTypeMapper = mockk<FilterTypeMapper>()
    private val getFilters = mockk<GetFilters>()
    private val saveFilter = mockk<SaveFilter>()
    private val navigator = mockk<Navigator>()
    override lateinit var viewModel: FilterContract.ViewModel

    override fun `setup subject`() {
        viewModel = FilterViewModel(
            filterTypeMapper,
            getFilters,
            saveFilter,
            navigator,
            schedulerProvider
        )
    }

    @BeforeEach
    @Test
    fun `should set title and then listing when initializing`() {
        val domainFilterType = FilterModel.Type.LOCATION_TYPE
        val getFilterParams = GetFilters.Params(domainFilterType)
        val filters = listOf("filter1", "filter2", "filter3")

        every { filterTypeMapper.mapToDomain(filterType) } returns domainFilterType
        every { getFilters(getFilterParams) } returns Single.just(filters)

        viewModel.initializeWithFilterType(filterType)

        assert(viewModel.filterState().value is FilterState.Title)
        assertEquals((viewModel.filterState().value as FilterState).titleRes, filterType.titleRes)

        testScheduler.triggerActions()

        assert(viewModel.filterState().value is FilterState.Listing)
        assertEquals((viewModel.filterState().value as FilterState).list, filters)

        verify(atLeast = 1, atMost = 2) {
            filterTypeMapper.mapToDomain(filterType)
            getFilters(getFilterParams)
        }
    }

    @Test
    fun `should set filter from text and post ClearSelection being able to apply when text is not empty`() {
        viewModel.setFilterFromText("text")

        assert(viewModel.filterState().value is FilterState.ClearSelection)
        assertEquals((viewModel.filterState().value as FilterState).canApply, true)

        verify(exactly = 1) { filterTypeMapper.mapToDomain(filterType) }
        confirmVerified(filterTypeMapper, getFilters, saveFilter, navigator)
    }

    @Test
    fun `should set filter from text and post ClearSelection not being able to apply when text is empty`() {
        viewModel.setFilterFromText("")

        assert(viewModel.filterState().value is FilterState.ClearSelection)
        assertEquals((viewModel.filterState().value as FilterState).canApply, false)

        verify(exactly = 1) { filterTypeMapper.mapToDomain(filterType) }
        confirmVerified(filterTypeMapper, getFilters, saveFilter, navigator)
    }

    @Test
    fun `should set filter from selection and post ClearText being able to apply when selection index exists`() {
        viewModel.setFilterFromSelection(0)

        assert(viewModel.filterState().value is FilterState.ClearText)
        assertEquals((viewModel.filterState().value as FilterState).canApply, true)

        verify(exactly = 1) { filterTypeMapper.mapToDomain(filterType) }
        confirmVerified(filterTypeMapper, getFilters, saveFilter, navigator)
    }

    @Test
    fun `should set filter from selection and post ClearText not being able to apply when selection index does not exist`() {
        viewModel.setFilterFromSelection(-1)

        assert(viewModel.filterState().value is FilterState.ClearText)
        assertEquals((viewModel.filterState().value as FilterState).canApply, false)

        verify(exactly = 1) { filterTypeMapper.mapToDomain(filterType) }
        confirmVerified(filterTypeMapper, getFilters, saveFilter, navigator)
    }

    @Test
    fun `should save filter and post result when getting it`() {
        val domainFilterType = FilterModel.Type.LOCATION_TYPE
        val text = "text"
        val saveFilterParams = SaveFilter.Params(text, domainFilterType)

        every { saveFilter(saveFilterParams) } returns Completable.complete()
        every { navigator.pop() } just runs

        assertNull(viewModel.filterResult().value)

        viewModel.setFilterFromText(text)
        viewModel.getFilter()

        testScheduler.triggerActions()

        assertEquals((viewModel.filterResult().value as FilterResult).type, filterType)
        assertEquals((viewModel.filterResult().value as FilterResult).value, text)

        verify(exactly = 2) { filterTypeMapper.mapToDomain(filterType) }
        verify(exactly = 1) {
            saveFilter(saveFilterParams)
            navigator.pop()
        }
        confirmVerified(filterTypeMapper, getFilters, saveFilter, navigator)
    }

    @Test
    fun `just pop navigation`() {
        every { navigator.pop() } just runs

        viewModel.close()

        verify(exactly = 1) {
            filterTypeMapper.mapToDomain(filterType)
            navigator.pop()
        }
        confirmVerified(filterTypeMapper, getFilters, saveFilter, navigator)
    }

    private companion object {
        val filterType = FilterType.LOCATION_TYPE
    }
}
