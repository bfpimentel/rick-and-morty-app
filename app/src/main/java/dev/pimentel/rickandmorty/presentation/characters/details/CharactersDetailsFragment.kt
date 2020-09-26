package dev.pimentel.rickandmorty.presentation.characters.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersDetailsFragmentBinding
import dev.pimentel.rickandmorty.presentation.characters.details.dto.CharacterDetails
import dev.pimentel.rickandmorty.shared.extensions.lifecycleBinding
import javax.inject.Inject

@AndroidEntryPoint
class CharactersDetailsFragment : Fragment(R.layout.characters_details_fragment) {

    @Inject
    lateinit var adapter: CharactersDetailsEpisodesAdapter

    private val binding by lifecycleBinding(CharactersDetailsFragmentBinding::bind)
    private val viewModel: CharactersDetailsContract.ViewModel by viewModels<CharactersDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindOutputs()
        bindInputs()
    }

    private fun bindOutputs() {
        binding.apply {
            episodes.also { list ->
                list.adapter = adapter
                list.layoutManager = LinearLayoutManager(context)
            }

            viewModel.characterDetails().observe(viewLifecycleOwner, Observer { details ->
                photo.load(details.image) { transformations(CircleCropTransformation()) }

                toolbar.title = details.name
                status.text = details.status
                name.text = details.name
                species.text = details.species
                gender.text = details.gender
                origin.text = details.origin
                type.text = details.type
                location.text = details.location
                adapter.submitList(details.episodes)
            })
        }
    }

    private fun bindInputs() {
        binding.apply {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener { viewModel.close() }
        }

        viewModel.initialize(
            requireArguments()[CHARACTERS_DETAILS_ARGUMENT_KEY] as CharacterDetails
        )
    }

    companion object {
        const val CHARACTERS_DETAILS_ARGUMENT_KEY = "CHARACTERS_DETAILS_ARGUMENT"
    }
}
