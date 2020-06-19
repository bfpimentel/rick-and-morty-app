package dev.pimentel.rickandmorty.presentation.locations

import androidx.fragment.app.Fragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.LocationsFragmentBinding
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding

class LocationsFragment : Fragment(R.layout.locations_fragment) {

    private val binding by lifecycleBinding(LocationsFragmentBinding::bind)
}
