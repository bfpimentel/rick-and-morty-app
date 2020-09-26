package dev.pimentel.rickandmorty.presentation.locations.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.LocationsFilterFragmentBinding
import dev.pimentel.rickandmorty.presentation.filter.FilterDialog
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter
import dev.pimentel.rickandmorty.shared.extensions.lifecycleBinding

@AndroidEntryPoint
class LocationsFilterFragment : BottomSheetDialogFragment() {

    private val binding by lifecycleBinding(LocationsFilterFragmentBinding::bind)
    private val viewModel: LocationsFilterContract.ViewModel by viewModels<LocationsFilterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.locations_filter_fragment, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindOutputs()
        bindInputs()
    }

    private fun bindOutputs() {
        binding.apply {
            viewModel.locationsFilterState().observe(
                viewLifecycleOwner,
                Observer { state ->
                    name.text = state.name
                    type.text = state.type
                    dimension.text = state.dimension
                    toolbar.menu.findItem(R.id.clear).isVisible = state.canClear
                    apply.isEnabled = state.canApplyFilter
                })

            viewModel.filteringResult().observe(viewLifecycleOwner, Observer { filter ->
                parentFragmentManager.setFragmentResult(
                    LOCATIONS_RESULT_LISTENER_KEY,
                    bundleOf(LOCATIONS_FILTER_RESULT_KEY to filter)
                )
            })
        }
    }

    private fun bindInputs() {
        binding.apply {
            nameContainer.setOnClickListener { viewModel.openNameFilter() }

            typeContainer.setOnClickListener { viewModel.openTypeFilter() }

            dimensionContainer.setOnClickListener { viewModel.openDimensionFilter() }

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
            requireArguments()[LOCATIONS_FILTER_ARGUMENT_KEY] as LocationsFilter
        )
    }

    companion object {
        const val LOCATIONS_FILTER_ARGUMENT_KEY = "LOCATIONS_FILTER_ARGUMENT"

        const val LOCATIONS_RESULT_LISTENER_KEY = "LOCATIONS_FILTER_RESULT_LISTENER"
        const val LOCATIONS_FILTER_RESULT_KEY = "LOCATIONS_FILTER_RESULT"
    }
}
