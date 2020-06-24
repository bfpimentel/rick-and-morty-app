package dev.pimentel.rickandmorty.presentation.characters.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersFilterFragmentBinding
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding

class CharactersFilterFragment : BottomSheetDialogFragment() {

    private val binding by lifecycleBinding(CharactersFilterFragmentBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.characters_filter_fragment, container)
}
