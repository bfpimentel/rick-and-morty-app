package dev.pimentel.rickandmorty.presentation.characters.filter.dto

data class CharactersFilter(
    val name: String? = null
) {

    companion object {
        val BLANK =
            CharactersFilter()
    }
}
