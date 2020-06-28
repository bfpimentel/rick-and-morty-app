package dev.pimentel.rickandmorty.presentation.episodes.dto

data class EpisodesItem(
    val id: Int,
    val name: String,
    val airDate: String,
    val number: String
) {

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
