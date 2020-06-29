package dev.pimentel.rickandmorty.presentation.episodes.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EpisodesItem(
    val id: Int,
    val name: String,
    val airDate: String,
    val number: String
) : Parcelable {

    companion object {
        const val GROUP_IDENTIFIER = -99

        fun group(name: String) = EpisodesItem(
            GROUP_IDENTIFIER,
            name,
            "",
            ""
        )
    }
}
