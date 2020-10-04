package dev.pimentel.rickandmorty.presentation.locations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.LocationsFragmentBinding
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsIntent
import dev.pimentel.rickandmorty.presentation.locations.dto.LocationsState
import dev.pimentel.rickandmorty.presentation.locations.filter.LocationsFilterFragment
import dev.pimentel.rickandmorty.presentation.locations.filter.dto.LocationsFilter
import dev.pimentel.rickandmorty.shared.extensions.lifecycleBinding
import dev.pimentel.rickandmorty.shared.helpers.EndOfScrollListener
import dev.pimentel.rickandmorty.shared.mvi.ReactiveViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LocationsFragment : Fragment(R.layout.locations_fragment) {

    @Inject
    lateinit var adapter: LocationsAdapter

    private val binding by lifecycleBinding(LocationsFragmentBinding::bind)
    private val viewModel: ReactiveViewModel<LocationsIntent, LocationsState>
            by viewModels<LocationsViewModel>()

    private lateinit var endOfScrollListener: EndOfScrollListener<StaggeredGridLayoutManager>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindOutputs()
        bindInputs()
    }


    private fun bindOutputs() {
        lifecycleScope.launch {
            viewModel.state().collect { state ->
                binding.apply {
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

                    toolbar.menu.findItem(R.id.filter).setIcon(state.filterIcon)
                }
            }
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
            { viewModel.intentChannel.offer(LocationsIntent.GetLocationsWithLastFilter) }
        )

        binding.apply {
            locationsList.also { list ->
                list.adapter = adapter
                list.layoutManager = layoutManager
                list.addOnScrollListener(endOfScrollListener)
            }

            toolbar.menu.findItem(R.id.filter).setOnMenuItemClickListener {
                viewModel.intentChannel.offer(LocationsIntent.OpenFilters)
                return@setOnMenuItemClickListener true
            }
        }

        setFragmentResultListener(
            LocationsFilterFragment.LOCATIONS_RESULT_LISTENER_KEY,
//            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.intentChannel.offer(
                LocationsIntent.GetLocations(
                    bundle[LocationsFilterFragment.LOCATIONS_FILTER_RESULT_KEY] as LocationsFilter
                )
            )
        }

        viewModel.intentChannel.offer(
            LocationsIntent.GetLocations(LocationsFilter.NO_FILTER)
        )
    }

    private companion object {
        const val LOCATIONS_ROW_COUNT = 2
    }
}
