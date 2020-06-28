package dev.pimentel.rickandmorty.presentation.characters.filter

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilterState
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult

interface CharactersFilterContract {

    interface ViewModel {
        fun initializeWithFilter(charactersFilter: CharactersFilter)
        fun openNameFilter()
        fun openSpeciesFilter()
        fun setTextFilter(filterResult: FilterResult)
        fun setStatus(@IdRes selectedOptionId: Int)
        fun setGender(@IdRes selectedOptionId: Int)
        fun clearFilter()
        fun getFilter()

        fun charactersFilterState(): LiveData<CharactersFilterState>
        fun filteringResult(): LiveData<CharactersFilter>
    }
}
