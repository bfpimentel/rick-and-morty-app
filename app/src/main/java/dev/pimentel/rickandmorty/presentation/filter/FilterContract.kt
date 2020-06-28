package dev.pimentel.rickandmorty.presentation.filter

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType

interface FilterContract {

    interface ViewModel {
        fun initializeWithFilterType(filterType: FilterType)
        fun getFilter()

        fun title(): LiveData<Int>
        fun filterList(): LiveData<List<String>>
        fun filterResult(): LiveData<FilterResult>
    }
}
