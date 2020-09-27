package dev.pimentel.rickandmorty.presentation.filter.dto

import androidx.annotation.StringRes

data class FilterState(
    @StringRes val titleRes: Int? = null,
    val list: List<String> = emptyList(),
    val clearText: Boolean = false,
    val clearSelection: Boolean = false,
    val canApply: Boolean = false,
    val result: FilterResult? = null
)
