package dev.pimentel.rickandmorty.presentation.characters.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.transform.CircleCropTransformation
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersDetailsFragmentBinding
import dev.pimentel.rickandmorty.presentation.characters.details.dto.CharacterDetails
import dev.pimentel.rickandmorty.shared.extensions.composeViewFor
import javax.inject.Inject

@AndroidEntryPoint
class CharactersDetailsFragment : Fragment() {

    @Inject
    lateinit var adapter: CharactersDetailsEpisodesAdapter
    private val viewModel: CharactersDetailsContract.ViewModel by viewModels<CharactersDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeViewFor { Screen() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindInputs()
    }

    @Composable
    fun Screen() {
        AndroidView(viewBlock = { context ->
            CharactersDetailsFragmentBinding.inflate(
                LayoutInflater.from(context),
            ).apply {
                episodes.also { list ->
                    list.adapter = adapter
                    list.layoutManager = LinearLayoutManager(context)
                }

                viewModel.characterDetails().observe(viewLifecycleOwner, { details ->
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

                toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
                toolbar.setNavigationOnClickListener { viewModel.close() }

                viewModel.initialize(
                    requireArguments()[CHARACTERS_DETAILS_ARGUMENT_KEY] as CharacterDetails
                )
            }.root
        })
    }

    private fun bindInputs() {
        viewModel.initialize(
            requireArguments()[CHARACTERS_DETAILS_ARGUMENT_KEY] as CharacterDetails
        )
    }

    companion object {
        const val CHARACTERS_DETAILS_ARGUMENT_KEY = "CHARACTERS_DETAILS_ARGUMENT"
    }
}
