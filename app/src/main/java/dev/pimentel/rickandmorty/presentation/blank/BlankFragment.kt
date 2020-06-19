package dev.pimentel.rickandmorty.presentation.blank

import androidx.fragment.app.Fragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.BlankFragmentBinding
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding

class BlankFragment : Fragment(R.layout.blank_fragment) {

    private val binding by lifecycleBinding(BlankFragmentBinding::bind)
}
