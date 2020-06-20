package dev.pimentel.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersFragmentBinding
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CharactersFragment : Fragment(R.layout.characters_fragment) {

    private val binding by lifecycleBinding(CharactersFragmentBinding::bind)
    private val viewModel: CharactersContract.ViewModel by viewModel<CharactersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(charactersModule)

        binding.fetch.setOnClickListener {
            viewModel.getCharacters()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(charactersModule)
    }
}
