package dev.pimentel.rickandmorty.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.domain.usecases.GetFilters
import dev.pimentel.domain.usecases.SaveFilter
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapper
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolder
import dev.pimentel.rickandmorty.shared.helpers.DisposablesHolderImpl
import dev.pimentel.rickandmorty.shared.navigator.NavigatorRouter
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import timber.log.Timber

class FilterViewModel(
    private val filterTypeMapper: FilterTypeMapper,
    private val getFilters: GetFilters,
    private val saveFilter: SaveFilter,
    private val navigator: NavigatorRouter,
    schedulerProvider: SchedulerProvider
) : ViewModel(),
    DisposablesHolder by DisposablesHolderImpl(schedulerProvider),
    FilterContract.ViewModel {

    private lateinit var filterType: FilterType
    private var filterValue: String? = null

    private val titleRes = MutableLiveData<Int>()
    private val filterList = MutableLiveData<List<String>>()
    private val filterResult = MutableLiveData<FilterResult>()

    override fun title(): LiveData<Int> = titleRes
    override fun filterList(): LiveData<List<String>> = filterList
    override fun filterResult(): LiveData<FilterResult> = filterResult

    override fun initializeWithFilterType(filterType: FilterType) {
        this.filterType = filterType
        titleRes.postValue(filterType.nameRes)

        getFilters(GetFilters.Params(filterTypeMapper.mapToDomain(filterType)))
            .compose(observeOnUIAfterSingleResult())
            .handle(filterList::postValue, Timber::d)
    }

    override fun getFilter() {
        saveFilter(
            SaveFilter.Params(
                "filterTest",
                filterTypeMapper.mapToDomain(filterType)
            )
        ).compose(observeOnUIAfterCompletableResult())
            .handle({
                filterResult.postValue(
                    FilterResult(
                        filterType,
                        "filterTest"
                    )
                )
                navigator.pop()
            }, Timber::d)
    }
}
