package dev.pimentel.rickandmorty.presentation.characters

import androidx.fragment.app.Fragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersFragmentBinding
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding

class CharactersFragment : Fragment(R.layout.characters_fragment) {

    private val binding by lifecycleBinding(CharactersFragmentBinding::bind)
}
