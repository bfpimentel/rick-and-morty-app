package dev.pimentel.rickandmorty.presentation.characters.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilterIntent
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilterState
import dev.pimentel.rickandmorty.presentation.filter.FilterDialog
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterResult
import dev.pimentel.rickandmorty.shared.extensions.composeViewFor
import dev.pimentel.rickandmorty.shared.mvi.ReactiveViewModel
import dev.pimentel.rickandmorty.shared.views.RadioGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CharactersFilterFragment : Fragment() {

    private val viewModel: ReactiveViewModel<CharactersFilterIntent, CharactersFilterState>
            by viewModels<CharactersFilterViewModel>()

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
        Scaffold(
            topBar = {
                TopAppBar(
                    elevation = 0.dp,
                    title = {
                        Text(
                            text = stringResource(id = R.string.top_filter),
                            color = Color.Black,
                            style = MaterialTheme.typography.subtitle1
                        )
                    },
                    actions = {
                        Text(
                            text = stringResource(id = R.string.filter_clear),
                            color = Color.Black,
                            modifier = Modifier.clickable(onClick = {
                                viewModel.intentChannel.offer(CharactersFilterIntent.ClearFilter)
                            })
                        )
                    }
                )
            },
            bottomBar = {
                Button(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    onClick = {
                        viewModel.intentChannel.offer(CharactersFilterIntent.ApplyFilter)
                    }
                ) {
                    Text(text = stringResource(id = R.string.characters_filter_apply))
                }
            }
        ) {
            Body()
        }
    }

    @Composable
    private fun Body() {
        val state = viewModel.state().collectAsState().value

        Column(modifier = Modifier.padding(all = 16.dp)) {
            Column(modifier = Modifier.clickable(onClick = {
                viewModel.intentChannel.offer(
                    CharactersFilterIntent.OpenNameFilter
                )
            })) {
                Text(
                    text = stringResource(id = R.string.characters_filter_name_label),
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.Black
                )
                Text(
                    text = state.name
                        ?: stringResource(id = R.string.characters_filter_name_hint),
                    style = MaterialTheme.typography.subtitle1
                )
                Divider(modifier = Modifier.padding(top = 16.dp))
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Column(modifier = Modifier.clickable(onClick = {
                viewModel.intentChannel.offer(
                    CharactersFilterIntent.OpenSpeciesFilter
                )
            })) {
                Text(
                    text = stringResource(id = R.string.characters_filter_species_label),
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.Black
                )
                Text(
                    text = state.species
                        ?: stringResource(id = R.string.characters_filter_species_hint),
                    style = MaterialTheme.typography.subtitle1
                )
                Divider(modifier = Modifier.padding(top = 16.dp))
            }
            Text(
                text = stringResource(id = R.string.characters_filter_status_label),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 16.dp)
            )
            RadioGroup(
                filters = listOf(
                    stringResource(id = R.id.status_alive),
                    stringResource(id = R.id.status_dead),
                    stringResource(id = R.id.status_unknown),
                ),
                selectedItemIndex = state.selectedStatusIndex,
                onItemClick = { index ->
                    viewModel.intentChannel.offer(
                        CharactersFilterIntent.SetStatus(
                            selectedStatusIndex = index
                        )
                    )
                }
            )
            Text(
                text = stringResource(id = R.string.characters_filter_status_label),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 16.dp)
            )
            RadioGroup(
                filters = listOf(
                    stringResource(id = R.id.gender_female),
                    stringResource(id = R.id.gender_male),
                    stringResource(id = R.id.gender_genderless),
                    stringResource(id = R.id.gender_unknown),
                ),
                selectedItemIndex = state.selectedGenderIndex,
                onItemClick = { index ->
                    viewModel.intentChannel.offer(
                        CharactersFilterIntent.SetGender(
                            selectedGenderIndex = index
                        )
                    )
                }
            )
        }

        state.result?.also { filter ->
            setFragmentResult(
                CHARACTERS_RESULT_LISTENER_KEY,
                bundleOf(CHARACTERS_FILTER_RESULT_KEY to filter)
            )
        }
    }

    private fun bindInputs() {
        setFragmentResultListener(
            FilterDialog.FILTER_RESULT_LISTENER_KEY,
//            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.intentChannel.offer(
                CharactersFilterIntent.SetTextFilter(
                    bundle[FilterDialog.FILTER_RESULT_KEY] as FilterResult
                )
            )
        }

        viewModel.intentChannel.offer(
            CharactersFilterIntent.Initialize(
                requireArguments()[CHARACTERS_FILTER_ARGUMENT_KEY] as CharactersFilter
            )
        )
    }

    companion object {
        const val CHARACTERS_FILTER_ARGUMENT_KEY = "CHARACTERS_FILTER_ARGUMENT"

        const val CHARACTERS_RESULT_LISTENER_KEY = "CHARACTERS_FILTER_RESULT_LISTENER"
        const val CHARACTERS_FILTER_RESULT_KEY = "CHARACTERS_FILTER_RESULT"
    }
}
