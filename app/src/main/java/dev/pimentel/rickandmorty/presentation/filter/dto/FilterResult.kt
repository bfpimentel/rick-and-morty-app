package dev.pimentel.rickandmorty.presentation.filter.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterResult(
    val resultKey: String,
    val value: String
) : Parcelable
