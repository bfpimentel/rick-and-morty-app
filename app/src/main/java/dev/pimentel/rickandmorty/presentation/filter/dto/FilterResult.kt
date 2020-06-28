package dev.pimentel.rickandmorty.presentation.filter.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterResult(
    val type: FilterType,
    val value: String
) : Parcelable
