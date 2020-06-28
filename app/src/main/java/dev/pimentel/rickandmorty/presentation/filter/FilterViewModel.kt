package dev.pimentel.rickandmorty.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.shared.navigator.NavigatorRouter

class FilterViewModel(
    private val navigator: NavigatorRouter
) : ViewModel(), FilterContract.ViewModel {

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

        // need to fetch filters on data source by type
    }

    override fun getFilter() {
        filterResult.postValue(
            FilterResult(
                filterType,
                "TESTE"
            )
        )
        navigator.pop()
    }
}
