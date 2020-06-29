package dev.pimentel.rickandmorty.presentation.episodes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.EpisodesFragmentBinding
import dev.pimentel.rickandmorty.presentation.episodes.filter.EpisodesFilterFragment
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter
import dev.pimentel.rickandmorty.shared.helpers.EndOfScrollListener
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class EpisodesFragment : Fragment(R.layout.episodes_fragment) {

    private val binding by lifecycleBinding(EpisodesFragmentBinding::bind)
    private val viewModel: EpisodesContract.ViewModel by viewModel<EpisodesViewModel>()
    private val adapter: EpisodesAdapter by inject()

    private lateinit var endOfScrollListener: EndOfScrollListener<LinearLayoutManager>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(episodesModule)
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(episodesModule)
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

            errorContainer.setOnClickListener { viewModel.getEpisodesWithLastFilter() }

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
