package dev.pimentel.rickandmorty.presentation.characters.data

data class CharactersFilter(
    val name: String? = null
) {

    companion object {
        val BLANK = CharactersFilter()
    }
}
