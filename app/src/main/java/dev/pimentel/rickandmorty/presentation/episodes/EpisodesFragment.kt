package dev.pimentel.rickandmorty.presentation.episodes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.EpisodesFragmentBinding
import dev.pimentel.rickandmorty.presentation.episodes.filter.EpisodesFilterFragment
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter
import dev.pimentel.rickandmorty.shared.helpers.EndOfScrollListener
import dev.pimentel.rickandmorty.shared.extensions.lifecycleBinding
import javax.inject.Inject

@AndroidEntryPoint
class EpisodesFragment : Fragment(R.layout.episodes_fragment) {

    @Inject
    lateinit var adapter: EpisodesAdapter

    private val binding by lifecycleBinding(EpisodesFragmentBinding::bind)
    private val viewModel: EpisodesContract.ViewModel by viewModels<EpisodesViewModel>()

    private lateinit var endOfScrollListener: EndOfScrollListener<LinearLayoutManager>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        endOfScrollListener.dispose()
    }

    private fun bindOutputs() {
        binding.apply {
            viewModel.episodesState().observe(viewLifecycleOwner, Observer { state ->
                adapter.submitList(state.episodes)
                state.scrollToTheTop?.also { episodesList.scrollToPosition(0) }
                state.errorMessage.also {
                    errorContainer.visibility = View.VISIBLE
                    errorMessage.text = state.errorMessage
                    episodesList.visibility = View.GONE
                } ?: run {
                    errorContainer.visibility = View.GONE
                    episodesList.visibility = View.VISIBLE
                }
            })

            viewModel.filterIcon().observe(viewLifecycleOwner, Observer { icon ->
                toolbar.menu.findItem(R.id.filter).setIcon(icon)
            })
        }
    }

    private fun bindInputs() {
        val layoutManager = LinearLayoutManager(context)

        endOfScrollListener = EndOfScrollListener(
            layoutManager,
            { false },
            { false },
            viewModel::getEpisodesWithLastFilter
        )

        binding.apply {
            episodesList.also { list ->
                list.adapter = adapter
                list.layoutManager = layoutManager
                list.addOnScrollListener(endOfScrollListener)
            }

            toolbar.menu.findItem(R.id.filter).setOnMenuItemClickListener {
                viewModel.openFilters()
                return@setOnMenuItemClickListener true
            }
        }

        parentFragmentManager.setFragmentResultListener(
            EpisodesFilterFragment.EPISODES_RESULT_LISTENER_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.getEpisodes(
                bundle[EpisodesFilterFragment.EPISODES_FILTER_RESULT_KEY] as EpisodesFilter
            )
        }

        viewModel.getEpisodes(EpisodesFilter.NO_FILTER)
    }
}
