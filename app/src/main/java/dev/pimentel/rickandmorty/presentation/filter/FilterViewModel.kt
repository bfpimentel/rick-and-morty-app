package dev.pimentel.rickandmorty.presentation.filter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.usecases.GetFilters
import dev.pimentel.domain.usecases.SaveFilter
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterState
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapper
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import timber.log.Timber

class FilterViewModel @ViewModelInject constructor(
    private val filterTypeMapper: FilterTypeMapper,
    private val getFilters: GetFilters,
    private val saveFilter: SaveFilter,
    private val navigator: Navigator,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    FilterContract.ViewModel {

    private lateinit var filterType: FilterType

    private var list: List<String>? = null
    private var filterValue: String? = null

    private val filterState = MutableLiveData<FilterState>()
    private val filterResult = MutableLiveData<FilterResult>()

    override fun filterState(): LiveData<FilterState> = filterState
    override fun filterResult(): LiveData<FilterResult> = filterResult

    override fun onCleared() {
        super.onCleared()
        disposeHolder()
    }

    override fun initializeWithFilterType(filterType: FilterType) {
        this.filterType = filterType
        filterState.postValue(FilterState.Title(filterType.titleRes))

        getFilters(GetFilters.Params(filterTypeMapper.mapToDomain(filterType)))
            .compose(observeOnUIAfterSingleResult())
            .handle({ list ->
                this.list = list

                filterState.postValue(
                    FilterState.Listing(
                        filterType.titleRes,
                        list
                    )
                )
            }, Timber::d)
    }

    override fun setFilterFromText(text: String) {
        filterValue = text
        filterState.postValue(
            FilterState.ClearSelection(
                filterType.titleRes,
                !filterValue.isNullOrBlank()
            )
        )
    }

    override fun setFilterFromSelection(index: Int) {
        filterValue = list?.getOrNull(index)
        filterState.postValue(
            FilterState.ClearText(
                filterType.titleRes,
                !filterValue.isNullOrBlank()
            )
        )
    }

    override fun getFilter() {
        saveFilter(
            SaveFilter.Params(
                filterValue!!,
                filterTypeMapper.mapToDomain(filterType)
            )
        ).compose(observeOnUIAfterCompletableResult())
            .handle({
                filterResult.postValue(
                    FilterResult(
                        filterType,
                        filterValue!!
                    )
                )
                navigator.pop()
            }, Timber::d)
    }

    override fun close() {
        navigator.pop()
    }
}
