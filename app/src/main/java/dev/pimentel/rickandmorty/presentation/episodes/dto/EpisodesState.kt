package dev.pimentel.rickandmorty.presentation.episodes.dto

import androidx.annotation.DrawableRes
import dev.pimentel.rickandmorty.R

data class EpisodesState(
    val episodes: List<EpisodesItem> = emptyList(),
    @DrawableRes val filterIcon: Int = R.drawable.ic_filter_default,
    val scrollToTheTop: Unit? = null,
    val errorMessage: String? = null
)
