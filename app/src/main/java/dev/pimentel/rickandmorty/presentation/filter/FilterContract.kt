package dev.pimentel.rickandmorty.presentation.filter

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterState
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType

interface FilterContract {

    interface ViewModel {
        fun initializeWithFilterType(filterType: FilterType)
        fun setFilterFromText(text: String)
        fun setFilterFromSelection(index: Int)
        fun getFilter()
        fun close()

        fun filterState(): LiveData<FilterState>
        fun filterResult(): LiveData<FilterResult>
    }
}
