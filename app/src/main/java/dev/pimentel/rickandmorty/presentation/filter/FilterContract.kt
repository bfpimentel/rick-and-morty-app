package dev.pimentel.rickandmorty.presentation.filter

import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType

interface FilterContract {

    interface ViewModel {
        fun initializeWithFilterType(filterType: FilterType)

        fun title(): LiveData<Int>
        fun filters(): LiveData<List<String>>
    }
}
