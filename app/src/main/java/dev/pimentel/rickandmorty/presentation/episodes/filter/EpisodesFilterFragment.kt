package dev.pimentel.rickandmorty.presentation.episodes.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.EpisodesFilterFragmentBinding
import dev.pimentel.rickandmorty.presentation.episodes.filter.dto.EpisodesFilter
import dev.pimentel.rickandmorty.presentation.filter.FilterDialog
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class EpisodesFilterFragment : BottomSheetDialogFragment() {

    private val binding by lifecycleBinding(EpisodesFilterFragmentBinding::bind)
    private val viewModel: EpisodesFilterContract.ViewModel by viewModel<EpisodesFilterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.episodes_filter_fragment, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(episodesFilterModule)
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(episodesFilterModule)
    }

    private fun bindOutputs() {
        binding.apply {
            viewModel.episodesFilterState().observe(
                viewLifecycleOwner,
                Observer { state ->
                    name.text = state.name
                    number.text = state.number
                    toolbar.menu.findItem(R.id.clear).isVisible = state.canClear
                    apply.isEnabled = state.canApplyFilter
                })

            viewModel.filteringResult().observe(viewLifecycleOwner, Observer { filter ->
                parentFragmentManager.setFragmentResult(
                    EPISODES_RESULT_LISTENER_KEY,
                    bundleOf(EPISODES_FILTER_RESULT_KEY to filter)
                )
            })
        }
    }

    private fun bindInputs() {
        binding.apply {
            nameContainer.setOnClickListener { viewModel.openNameFilter() }

            numberContainer.setOnClickListener { viewModel.openNumberFilter() }

            toolbar.menu.findItem(R.id.clear).setOnMenuItemClickListener {
                viewModel.clearFilter()
                return@setOnMenuItemClickListener true
            }

            apply.setOnClickListener { viewModel.getFilter() }
        }

        parentFragmentManager.setFragmentResultListener(
            FilterDialog.FILTER_RESULT_LISTENER_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.setTextFilter(
                bundle[FilterDialog.FILTER_RESULT_KEY] as FilterResult
            )
        }

        viewModel.initializeWithFilter(
            requireArguments()[EPISODES_FILTER_ARGUMENT_KEY] as EpisodesFilter
        )
    }

    companion object {
        const val EPISODES_FILTER_ARGUMENT_KEY = "EPISODES_FILTER_ARGUMENT"

        const val EPISODES_RESULT_LISTENER_KEY = "EPISODES_FILTER_RESULT_LISTENER"
        const val EPISODES_FILTER_RESULT_KEY = "EPISODES_FILTER_RESULT"
    }
}
