package dev.pimentel.rickandmorty.presentation.characters.details.dto

import android.os.Parcelable
import dev.pimentel.rickandmorty.presentation.episodes.dto.EpisodesItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterDetails(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String,
    val gender: String,
    val origin: String,
    val type: String,
    val location: String,
    val episodes: List<EpisodesItem>
) : Parcelable
