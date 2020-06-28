package dev.pimentel.rickandmorty.presentation.characters.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersFilterFragmentBinding
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.filter.FilterDialog
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CharactersFilterFragment : BottomSheetDialogFragment() {

    private val binding by lifecycleBinding(CharactersFilterFragmentBinding::bind)
    private val viewModel: CharactersFilterContract.ViewModel by viewModel<CharactersFilterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.characters_filter_fragment, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(charactersFilterModule)
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(charactersFilterModule)
    }

    private fun bindOutputs() {
        binding.apply {
            viewModel.charactersFilterState().observe(
                viewLifecycleOwner,
                Observer { state ->
                    name.text = state.name
                    species.text = state.species
                    toolbar.menu.findItem(R.id.clear).isVisible = state.canClear
                    state.statusId?.also(statusGroup::check) ?: statusGroup.clearCheck()
                    state.genderId?.also(genderGroup::check) ?: genderGroup.clearCheck()
                    apply.isEnabled = state.canApplyFilter
                })

            viewModel.filteringResult().observe(viewLifecycleOwner, Observer { filter ->
                parentFragmentManager.setFragmentResult(
                    CHARACTERS_RESULT_LISTENER_KEY,
                    bundleOf(CHARACTERS_FILTER_RESULT_KEY to filter)
                )
            })
        }
    }

    private fun bindInputs() {
        binding.apply {
            nameContainer.setOnClickListener { viewModel.openNameFilter() }

            speciesContainer.setOnClickListener { viewModel.openSpeciesFilter() }

            statusGroup.setOnCheckedChangeListener { _, checkedId ->
                viewModel.setStatus(checkedId)
            }

            genderGroup.setOnCheckedChangeListener { _, checkedId ->
                viewModel.setGender(checkedId)
            }

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
            requireArguments()[CHARACTERS_FILTER_ARGUMENT_KEY] as CharactersFilter
        )
    }

    companion object {
        const val CHARACTERS_FILTER_ARGUMENT_KEY = "CHARACTERS_FILTER_ARGUMENT"

        const val CHARACTERS_RESULT_LISTENER_KEY = "CHARACTERS_FILTER_RESULT_LISTENER"
        const val CHARACTERS_FILTER_RESULT_KEY = "CHARACTERS_FILTER_RESULT"
    }
}
