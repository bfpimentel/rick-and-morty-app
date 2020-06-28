package dev.pimentel.rickandmorty.presentation.filter

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.FilterDialogBinding
import dev.pimentel.rickandmorty.databinding.FilterItemBinding
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import timber.log.Timber

class FilterDialog : DialogFragment(R.layout.filter_dialog) {

    private val binding by lifecycleBinding(FilterDialogBinding::bind)
    private val viewModel: FilterContract.ViewModel by viewModel<FilterViewModel>()

    private lateinit var searchFieldListener: TextWatcher

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
            viewModel.filterState().observe(viewLifecycleOwner, Observer { state ->
                title.setText(state.titleRes)
                state.clearText ?: run {
                    searchField.removeTextChangedListener(searchFieldListener)
                    searchField.text = null
                    searchField.addTextChangedListener(searchFieldListener)
                }
                state.clearSelection ?: run { oldFilters.clearCheck() }
                state.list.mapIndexed { index, filter ->
                    val binding = FilterItemBinding.inflate(layoutInflater, oldFilters, false)
                    binding.item.text = filter
                    binding.item.id = index
                    binding.root
                }.forEach(oldFilters::addView)
                ok.isEnabled = state.canApply
            })
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

            searchFieldListener = searchField.doAfterTextChanged { text ->
                viewModel.setFilterFromText(text.toString())
            }

            oldFilters.setOnCheckedChangeListener { group, index ->
                try {
                    if ((group[index] as RadioButton).isChecked) {
                        viewModel.setFilterFromSelection(index)
                    }
                } catch (exception: IndexOutOfBoundsException) {
                    Timber.d(exception)
                }
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
