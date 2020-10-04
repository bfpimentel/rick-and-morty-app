package dev.pimentel.rickandmorty.presentation.filter.dto

import androidx.annotation.StringRes

data class FilterState(
    @StringRes val titleRes: Int? = null,
    val filters: List<String> = emptyList(),
    val selectedItemIndex: Int? = null,
    val inputText: String = "",
    val canApply: Boolean = false,
    val result: FilterResult? = null
)
