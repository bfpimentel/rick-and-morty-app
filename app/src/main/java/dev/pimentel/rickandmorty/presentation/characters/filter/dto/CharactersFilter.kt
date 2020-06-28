package dev.pimentel.rickandmorty.presentation.characters.filter.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharactersFilter(
    val name: String? = null,
    val species: String? = null,
    val status: String? = null,
    val gender: String? = null
) : Parcelable {

    companion object {
        val NO_FILTER = CharactersFilter()
    }
}
