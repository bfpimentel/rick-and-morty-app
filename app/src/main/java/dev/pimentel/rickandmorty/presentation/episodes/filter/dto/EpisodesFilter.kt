package dev.pimentel.rickandmorty.presentation.episodes.filter.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EpisodesFilter(
    val name: String? = null,
    val number: String? = null
) : Parcelable {

    companion object {
        val NO_FILTER = EpisodesFilter()
    }
}
