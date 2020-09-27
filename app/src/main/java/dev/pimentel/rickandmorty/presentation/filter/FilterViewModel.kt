package dev.pimentel.rickandmorty.presentation.filter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pimentel.domain.usecases.GetFilters
import dev.pimentel.domain.usecases.SaveFilter
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterIntent
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterState
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapper
import dev.pimentel.rickandmorty.shared.dispatchersprovider.DispatchersProvider
import dev.pimentel.rickandmorty.shared.extensions.throttleFirst
import dev.pimentel.rickandmorty.shared.mvi.Reducer
import dev.pimentel.rickandmorty.shared.mvi.ReducerImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class FilterViewModel @ViewModelInject constructor(
    private val filterTypeMapper: FilterTypeMapper,
    private val getFilters: GetFilters,
    private val saveFilter: SaveFilter,
    private val navigator: Navigator,
    dispatchersProvider: DispatchersProvider
) : ViewModel(), FilterContract.ViewModel, Reducer<FilterState> by ReducerImpl(FilterState()) {

    private lateinit var filterType: FilterType

    private var list: List<String>? = null
    private var filterValue: String? = null

    override val intentChannel: Channel<FilterIntent> = Channel(Channel.UNLIMITED)

    override fun state(): StateFlow<FilterState> = mutableState

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            intentChannel.consumeAsFlow().throttleFirst().collect { intent ->
                when (intent) {
                    is FilterIntent.Initialize -> initializeWithFilterType(intent.filterType)
                    is FilterIntent.SetFilterFromText -> setFilterFromText(intent.text)
                    is FilterIntent.SetFilterFromSelection -> setFilterFromSelection(intent.index)
                    is FilterIntent.GetFilter -> getFilter()
                    is FilterIntent.Close -> navigator.pop()
                }
            }
        }
    }

    private suspend fun initializeWithFilterType(filterType: FilterType) {
        this.filterType = filterType

        updateState { copy(titleRes = filterType.titleRes) }

        try {
            val filters = getFilters(
                GetFilters.Params(filterTypeMapper.mapToDomain(filterType))
            )

            this.list = filters

            updateState { copy(list = filters) }
        } catch (exception: Exception) {
            Timber.d(exception)
        }
    }

    private suspend fun setFilterFromText(text: String) {
        filterValue = text
        updateState {
            copy(
                clearSelection = !filterValue.isNullOrBlank()
            )
        }
    }

    private suspend fun setFilterFromSelection(index: Int) {
        filterValue = list?.getOrNull(index)
        updateState {
            copy(
                clearText = !filterValue.isNullOrBlank()
            )
        }
    }

    private suspend fun getFilter() {
        try {
            saveFilter(
                SaveFilter.Params(
                    filterValue!!,
                    filterTypeMapper.mapToDomain(filterType)
                )
            )

            updateState {
                copy(
                    result = FilterResult(
                        filterType,
                        filterValue!!
                    )
                )
            }

            navigator.pop()
        } catch (exception: Exception) {
            Timber.d(exception)
        }
    }
}
