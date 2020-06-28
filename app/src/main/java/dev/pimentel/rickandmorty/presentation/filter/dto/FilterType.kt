package dev.pimentel.rickandmorty.presentation.filter.dto

import android.os.Parcelable
import androidx.annotation.StringRes
import dev.pimentel.rickandmorty.R
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class FilterType(
    @StringRes val nameRes: Int
) : Parcelable {

    CHARACTER_NAME(R.string.filter_character_name),
    CHARACTER_SPECIES(R.string.filter_character_species),
    LOCATION_NAME(R.string.filter_location_name),
    LOCATION_TYPE(R.string.filter_location_type),
    LOCATION_DIMENSION(R.string.filter_location_dimension),
    EPISODE_NAME(R.string.filter_episode_name),
    EPISODE_NUMBER(R.string.filter_episode_number)
}
