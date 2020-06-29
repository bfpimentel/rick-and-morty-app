package dev.pimentel.rickandmorty.presentation.episodes.dto

sealed class EpisodesState(
    val episodes: List<EpisodesItem> = emptyList(),
    val errorMessage: String? = null
) {

    class Empty() : EpisodesState()

    class Success(
        episodes: List<EpisodesItem>
    ) : EpisodesState(episodes = episodes)

    class Error(
        errorMessage: String
    ) : EpisodesState(errorMessage = errorMessage)
}
