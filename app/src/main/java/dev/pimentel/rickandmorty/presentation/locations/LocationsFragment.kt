package dev.pimentel.rickandmorty.presentation.locations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.LocationsFragmentBinding
import dev.pimentel.rickandmorty.presentation.locations.filter.LocationsFilterFragment
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter
import dev.pimentel.rickandmorty.shared.helpers.EndOfScrollListener
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import javax.inject.Inject

@AndroidEntryPoint
class LocationsFragment : Fragment(R.layout.locations_fragment) {

    @Inject
    lateinit var adapter: LocationsAdapter

    private val binding by lifecycleBinding(LocationsFragmentBinding::bind)
    private val viewModel: LocationsContract.ViewModel by viewModels<LocationsViewModel>()

    private lateinit var endOfScrollListener: EndOfScrollListener<StaggeredGridLayoutManager>

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
            viewModel.locationsState().observe(viewLifecycleOwner, Observer { state ->
                adapter.submitList(state.locations)
                state.scrollToTheTop?.also { locationsList.scrollToPosition(0) }
                state.errorMessage?.also {
                    errorContainer.visibility = View.VISIBLE
                    errorMessage.text = state.errorMessage
                    locationsList.visibility = View.GONE
                } ?: run {
                    errorContainer.visibility = View.GONE
                    locationsList.visibility = View.VISIBLE
                }
            })

            viewModel.filterIcon().observe(viewLifecycleOwner, Observer { icon ->
                toolbar.menu.findItem(R.id.filter).setIcon(icon)
            })
        }
    }

    private fun bindInputs() {
        val layoutManager = StaggeredGridLayoutManager(
            LOCATIONS_ROW_COUNT,
            RecyclerView.VERTICAL
        )

        endOfScrollListener = EndOfScrollListener(
            layoutManager,
            { false },
            { false },
            viewModel::getLocationsWithLastFilter
        )

        binding.apply {
            locationsList.also { list ->
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
            LocationsFilterFragment.LOCATIONS_RESULT_LISTENER_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.getLocations(
                bundle[LocationsFilterFragment.LOCATIONS_FILTER_RESULT_KEY] as LocationsFilter
            )
        }

        viewModel.getLocations(LocationsFilter.NO_FILTER)
    }

    private companion object {
        const val LOCATIONS_ROW_COUNT = 2
    }
}
