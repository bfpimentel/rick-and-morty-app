package dev.pimentel.rickandmorty.presentation.filter.dto

import android.os.Parcelable
import androidx.annotation.StringRes
import dev.pimentel.rickandmorty.R
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class FilterType(
    val resultKey: String,
    @StringRes val nameRes: Int
) : Parcelable {

    CHARACTER_NAME("FILTER_CHARACTER_NAME", R.string.filter_character_name),
    CHARACTER_SPECIES("FILTER_CHARACTER_SPECIES", R.string.filter_character_species),
    LOCATION_NAME("FILTER_LOCATION_NAME", R.string.filter_location_name),
    LOCATION_TYPE("FILTER_LOCATION_TYPE", R.string.filter_location_type),
    LOCATION_DIMENSION("FILTER_LOCATION_DIMENSION", R.string.filter_location_dimension),
    EPISODE_NAME("FILTER_EPISODE_NAME", R.string.filter_episode_name),
    EPISODE_NUMBER("FILTER_EPISODE_NUMBER", R.string.filter_episode_number)
}
