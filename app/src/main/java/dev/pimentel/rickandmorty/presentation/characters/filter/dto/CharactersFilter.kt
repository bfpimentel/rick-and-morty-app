package dev.pimentel.rickandmorty.presentation.characters.filter.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharactersFilter(
    val name: String? = null,
    val status: String? = null,
    val gender: String? = null
) : Parcelable {

    companion object {
        val NO_FILTER = CharactersFilter()

        const val RESULT_KEY = "CHARACTERS_FILTER_REQUEST"

        // must be equal to the one that is inside main_navigation_graph.xml
        const val ARGUMENT_NAME = "CHARACTERS_FILTER_ARGUMENT"
    }
}
