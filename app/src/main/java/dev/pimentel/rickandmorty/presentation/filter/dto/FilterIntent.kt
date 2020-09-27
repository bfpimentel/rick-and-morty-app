package dev.pimentel.rickandmorty.presentation.filter.dto

sealed class FilterIntent {

    data class Initialize(val filterType: FilterType) : FilterIntent()

    data class SetFilterFromText(val text: String) : FilterIntent()

    data class SetFilterFromSelection(val index: Int) : FilterIntent()

    object GetFilter : FilterIntent()

    object Close : FilterIntent()
}
