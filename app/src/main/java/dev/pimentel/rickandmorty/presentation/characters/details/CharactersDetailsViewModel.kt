package dev.pimentel.rickandmorty.presentation.characters.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.rickandmorty.presentation.characters.details.dto.CharacterDetails
import dev.pimentel.rickandmorty.shared.navigator.NavigatorRouter

class CharactersDetailsViewModel(
    private val navigator: NavigatorRouter
) : ViewModel(), CharactersDetailsContract.ViewModel {

    private val characterDetails = MutableLiveData<CharacterDetails>()

    override fun characterDetails(): LiveData<CharacterDetails> = characterDetails

    override fun initialize(characterDetails: CharacterDetails) {
        this.characterDetails.postValue(characterDetails)
    }

    override fun close() {
        navigator.pop()
    }
}
