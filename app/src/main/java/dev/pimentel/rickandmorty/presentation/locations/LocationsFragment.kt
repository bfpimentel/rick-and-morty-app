package dev.pimentel.rickandmorty.presentation.locations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.LocationsFragmentBinding
import dev.pimentel.rickandmorty.shared.helpers.EndOfScrollListener
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class LocationsFragment : Fragment(R.layout.locations_fragment) {

    private val binding by lifecycleBinding(LocationsFragmentBinding::bind)
    private val viewModel: LocationsContract.ViewModel by viewModel<LocationsViewModel>()
    private val adapter: LocationsAdapter by inject()

    private lateinit var endOfScrollListener: EndOfScrollListener<StaggeredGridLayoutManager>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(locationsModule)
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(locationsModule)
        endOfScrollListener.dispose()
    }

    private fun bindOutputs() {
        val layoutManager = StaggeredGridLayoutManager(
            LOCATIONS_ROW_COUNT,
            RecyclerView.VERTICAL
        )

        endOfScrollListener = EndOfScrollListener(
            layoutManager,
            { false },
            { false },
            viewModel::getMoreLocations
        )
    }

    private fun bindInputs() {

    }

    private companion object {
        const val LOCATIONS_ROW_COUNT = 2
    }
}
