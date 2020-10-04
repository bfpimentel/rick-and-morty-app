package dev.pimentel.rickandmorty.presentation.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterIntent
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterState
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType
import dev.pimentel.rickandmorty.shared.extensions.composeViewFor
import dev.pimentel.rickandmorty.shared.mvi.ReactiveViewModel
import dev.pimentel.rickandmorty.shared.views.RadioGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FilterDialog : DialogFragment() {

    private val viewModel: ReactiveViewModel<FilterIntent, FilterState>
            by viewModels<FilterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeViewFor { Screen() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindInputs()
    }

    @Composable
    private fun Screen() {
        val state = viewModel.state().collectAsState().value

        Column {
            state.titleRes?.let {
                Text(
                    text = stringResource(id = it),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            TextField(
                value = state.inputText,
                placeholder = {
                    Text(text = stringResource(id = R.string.filter_dialog_search_hint))
                },
                onValueChange = { text ->
                    viewModel.intentChannel.offer(FilterIntent.SetFilterFromText(text))
                }
            )
            RadioGroup(
                filters = state.filters,
                selectedItemIndex = state.selectedItemIndex,
                onItemClick = { index ->
                    viewModel.intentChannel.offer(
                        FilterIntent.SetFilterFromSelection(
                            index = index
                        )
                    )
                }
            )
            Row(modifier = Modifier.fillMaxWidth().align(alignment = Alignment.End)) {
                Button(onClick = {
                    viewModel.intentChannel.offer(FilterIntent.Close)
                }) {
                    Text(
                        text = stringResource(id = R.string.filter_dialog_cancel),
                        style = MaterialTheme.typography.button
                    )
                }
                Button(
                    enabled = state.canApply,
                    onClick = {
                        viewModel.intentChannel.offer(FilterIntent.GetFilter)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.filter_dialog_ok),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }

        state.result?.let { result ->
            parentFragmentManager.setFragmentResult(
                FILTER_RESULT_LISTENER_KEY,
                bundleOf(FILTER_RESULT_KEY to result)
            )
        }
    }

    private fun bindInputs() {
        viewModel.intentChannel.offer(
            FilterIntent.Initialize(
                requireArguments()[FILTER_TYPE_ARGUMENT_KEY] as FilterType
            )
        )
    }

    companion object {
        const val FILTER_TYPE_ARGUMENT_KEY = "FILTER_TYPE_ARGUMENT"

        const val FILTER_RESULT_LISTENER_KEY = "FILTER_RESULT_LISTENER"
        const val FILTER_RESULT_KEY = "FILTER_RESULT"
    }
}
