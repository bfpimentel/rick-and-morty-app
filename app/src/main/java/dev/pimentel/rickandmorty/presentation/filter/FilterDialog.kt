package dev.pimentel.rickandmorty.presentation.filter

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.FilterDialogBinding
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class FilterDialog : DialogFragment(R.layout.filter_dialog) {

    private val binding by lifecycleBinding(FilterDialogBinding::bind)
    private val viewModel: FilterContract.ViewModel by viewModel<FilterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(filterModule)
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(filterModule)
    }

    private fun bindOutputs() {
        binding.apply {
            viewModel.title().observe(viewLifecycleOwner, Observer(title::setText))
        }

        viewModel.filterResult().observe(viewLifecycleOwner, Observer { result ->
            parentFragmentManager.setFragmentResult(
                FILTER_RESULT_LISTENER_KEY,
                bundleOf(FILTER_RESULT_KEY to result)
            )
        })
    }

    private fun bindInputs() {
        binding.apply {
            ok.setOnClickListener {
                viewModel.getFilter()
            }
        }

        viewModel.initializeWithFilterType(
            requireArguments()[FILTER_TYPE_ARGUMENT_KEY] as FilterType
        )
    }

    companion object {
        const val FILTER_TYPE_ARGUMENT_KEY = "FILTER_TYPE_ARGUMENT"

        const val FILTER_RESULT_LISTENER_KEY = "FILTER_RESULT_LISTENER"
        const val FILTER_RESULT_KEY = "FILTER_RESULT"
    }
}
