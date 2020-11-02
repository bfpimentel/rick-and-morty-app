package dev.pimentel.rickandmorty.presentation.characters.dto

import androidx.annotation.DrawableRes
import dev.pimentel.rickandmorty.R

data class CharactersState(
    val characters: List<CharactersItem> = emptyList(),
    @DrawableRes val filterIcon: Int = R.drawable.ic_filter_default,
    val listErrorMessage: String? = null,
    val detailsErrorMessage: String? = null,
) {

    companion object {
        @JvmStatic
        val INITIAL = CharactersState(
            characters = emptyList(),
            filterIcon = R.drawable.ic_filter_default,
            listErrorMessage = null,
            detailsErrorMessage = null,
        )
    }
}
