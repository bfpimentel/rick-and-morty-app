package dev.pimentel.rickandmorty.presentation.characters.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilterState
import dev.pimentel.rickandmorty.shared.navigator.NavigatorRouter

class CharactersFilterViewModel(
    private val navigator: NavigatorRouter
) : ViewModel(),
    CharactersFilterContract.ViewModel {

    private lateinit var lastFilter: CharactersFilter
    private lateinit var currentFilter: CharactersFilter

    private val charactersFilterState = MutableLiveData<CharactersFilterState>()
    private val filteringResult = MutableLiveData<CharactersFilter>()

    override fun charactersFilterState(): LiveData<CharactersFilterState> = charactersFilterState
    override fun filteringResult(): LiveData<CharactersFilter> = filteringResult

    override fun initializeWithFilter(charactersFilter: CharactersFilter) {
        this.lastFilter = charactersFilter
        this.currentFilter = charactersFilter.copy()
        buildFilterState()
    }

    override fun setName(name: String) {
        this.currentFilter = this.currentFilter.copy(
            name = name
        )
        buildFilterState()
    }

    override fun setStatus(selectedOptionId: Int) {
        this.currentFilter = this.currentFilter.copy(
            status = STATUS_MAP.firstOrNull { pair -> pair.second == selectedOptionId }?.first
        )
        buildFilterState()
    }

    override fun setGender(selectedOptionId: Int) {
        this.currentFilter = this.currentFilter.copy(
            gender = GENDER_MAP.firstOrNull { pair -> pair.second == selectedOptionId }?.first
        )
        buildFilterState()
    }

    override fun clearFilter() {
        this.currentFilter = CharactersFilter.NO_FILTER
        buildFilterState()
    }

    override fun getFilter() {
        filteringResult.postValue(currentFilter)
        navigator.pop()
    }

    private fun buildFilterState() {
        charactersFilterState.postValue(
            CharactersFilterState(
                lastFilter != currentFilter,
                currentFilter != CharactersFilter.NO_FILTER,
                currentFilter.name,
                STATUS_MAP.firstOrNull { pair -> pair.first == currentFilter.status }?.second,
                GENDER_MAP.firstOrNull { pair -> pair.first == currentFilter.gender }?.second
            )
        )
    }

    private companion object {
        val STATUS_MAP = setOf(
            "alive" to R.id.status_alive,
            "dead" to R.id.status_dead,
            "unknown" to R.id.status_unknown
        )

        val GENDER_MAP = setOf(
            "female" to R.id.gender_female,
            "male" to R.id.gender_male,
            "genderless" to R.id.gender_genderless,
            "unknown" to R.id.gender_unknown
        )
    }
}
