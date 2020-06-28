package dev.pimentel.rickandmorty.presentation.locations.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.LocationsFilterFragmentBinding
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class LocationsFilterFragment : BottomSheetDialogFragment() {

    private val binding by lifecycleBinding(LocationsFilterFragmentBinding::bind)
    private val viewModel: LocationsFilterContract.ViewModel by viewModel<LocationsFilterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.locations_filter_fragment, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(locationsFilterModule)
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(locationsFilterModule)
    }

    private fun bindOutputs() {

    }

    private fun bindInputs() {

    }

    companion object {
        const val LOCATIONS_FILTER_ARGUMENT_KEY = "LOCATIONS_FILTER_ARGUMENT"

        const val LOCATIONS_RESULT_LISTENER_KEY = "LOCATIONS_FILTER_RESULT_LISTENER"
        const val LOCATIONS_FILTER_RESULT_KEY = "LOCATIONS_FILTER_RESULT"
    }
}
