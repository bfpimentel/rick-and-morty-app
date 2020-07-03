package dev.pimentel.rickandmorty.presentation.filter.dto

import androidx.annotation.StringRes

sealed class FilterState(
    @StringRes val titleRes: Int,
    val list: List<String> = emptyList(),
    val clearText: Unit? = null,
    val clearSelection: Unit? = null,
    val canApply: Boolean = false
) {

    class Title(
        titleRes: Int
    ) : FilterState(titleRes = titleRes)

    class Listing(
        titleRes: Int,
        list: List<String>
    ) : FilterState(
        titleRes = titleRes,
        list = list
    )

    class ClearText(
        titleRes: Int,
        canApply: Boolean
    ) : FilterState(
        titleRes = titleRes,
        canApply = canApply,
        clearText = Unit
    )

    class ClearSelection(
        titleRes: Int,
        canApply: Boolean
    ) : FilterState(
        titleRes = titleRes,
        canApply = canApply,
        clearSelection = Unit
    )
}
