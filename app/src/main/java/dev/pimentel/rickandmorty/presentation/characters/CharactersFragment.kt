package dev.pimentel.rickandmorty.presentation.characters

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.ColumnScope.weight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.ui.tooling.preview.Preview
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersItemBinding
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersIntent
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.filter.CharactersFilterFragment
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.shared.helpers.composeViewFor
import dev.pimentel.rickandmorty.shared.views.LazyVerticalGridForIndexed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.characters_fragment) {

    @Inject
    lateinit var adapter: CharactersAdapter
    private val viewModel: CharactersContract.ViewModel by viewModels<CharactersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = composeViewFor { Screen() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindInputs()
    }

    @Preview(name = "Screen")
    @Composable
    private fun Screen() {
        val charactersState = viewModel.charactersState().collectAsState().value
        val filterIcon = viewModel.filterIcon().collectAsState().value
        val error = viewModel.error().collectAsState().value

        MaterialTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.characters_title)) },
                        actions = {
                            IconButton(onClick = {
                                viewModel.intentChannel.offer(CharactersIntent.OpenFilters)
                            }) {
                                Icon(asset = vectorResource(id = filterIcon))
                            }
                        }
                    )
                }
            ) {
                when (charactersState) {
                    is CharactersState.Success -> SuccessScreen(value = charactersState)
                    is CharactersState.Empty -> {
                    }
                    is CharactersState.Error -> {
                    }
                }
            }
        }

        error?.also(::showErrorDialog)
    }

    @Composable
    private fun SuccessScreen(value: CharactersState.Success) {
        LazyVerticalGridForIndexed(
            items = value.characters,
            perRow = CHARACTERS_ROW_COUNT
        ) { index, character ->
            AndroidView(
                modifier = Modifier.weight(1f).padding(8.dp),
                viewBlock = {
                    // TODO: Need to change ViewBinding view to a Composable one.
                    CharactersItemBinding.inflate(
                        LayoutInflater.from(it)
                    ).apply {
                        root.setOnClickListener {
                            viewModel.intentChannel.offer(
                                CharactersIntent.GetDetails(
                                    characterId = character.id
                                )
                            )
                        }

                        image.load(character.image)
                        status.text = character.status
                        name.text = character.name
                    }.root
                })

            if (index == value.characters.lastIndex) {
                viewModel.intentChannel.offer(CharactersIntent.GetCharactersWithLastFilter)
            }
        }
    }

    private fun bindInputs() {
        parentFragmentManager.setFragmentResultListener(
            CharactersFilterFragment.CHARACTERS_RESULT_LISTENER_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.intentChannel.offer(
                CharactersIntent.GetCharacters(
                    bundle.get(
                        CharactersFilterFragment.CHARACTERS_FILTER_RESULT_KEY
                    ) as CharactersFilter
                )
            )
        }

        viewModel.intentChannel.offer(
            CharactersIntent.GetCharacters(
                filter = CharactersFilter.NO_FILTER
            )
        )
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_dialog_title)
            .setMessage(errorMessage)
            .create()
            .show()
    }

    private companion object {
        const val CHARACTERS_ROW_COUNT = 2
    }
}
