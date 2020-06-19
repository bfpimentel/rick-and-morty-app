package dev.pimentel.rickandmorty.presentation.episodes

import androidx.fragment.app.Fragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.EpisodesFragmentBinding
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding

class EpisodesFragment : Fragment(R.layout.episodes_fragment) {

    private val binding by lifecycleBinding(EpisodesFragmentBinding::bind)
}
