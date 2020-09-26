package dev.pimentel.rickandmorty.presentation.characters

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope.weight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
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
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersIntent
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersItem
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.filter.CharactersFilterFragment
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.shared.extensions.composeViewFor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CharactersFragment : Fragment() {

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
        val charactersState = viewModel.state().collectAsState().value

        with(charactersState) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        elevation = 0.dp,
                        title = {
                            Text(
                                text = stringResource(id = R.string.characters_title),
                                style = MaterialTheme.typography.h6,
                                color = Color.Black
                            )
                        },
                        actions = {
                            IconButton(onClick = {
                                viewModel.intentChannel.offer(CharactersIntent.OpenFilters)
                            }) {
                                Icon(
                                    asset = vectorResource(id = filterIcon),
                                    tint = Color.Black
                                )
                            }
                        }
                    )
                }
            ) {
                CharactersList(state = charactersState)
            }

            detailsErrorMessage?.also(::showErrorDialog)
        }
    }

    @Composable
    private fun CharactersList(state: CharactersState) {
        LazyColumnForIndexed(items = state.characters) { index, character ->
            Character(item = character)

            if (index == state.characters.lastIndex) {
                viewModel.intentChannel.offer(CharactersIntent.GetCharactersWithLastFilter)
            }
        }

//        LazyVerticalGridForIndexed(
//            items = state.characters,
//            perRow = CHARACTERS_ROW_COUNT
//        ) { index, character ->
//            Character(item = character)
//
//            if (index == state.characters.lastIndex) {
//                viewModel.intentChannel.offer(CharactersIntent.GetCharactersWithLastFilter)
//            }
//        }
    }

    @Composable
    private fun Character(item: CharactersItem) {
        Card(
            modifier = Modifier.weight(1f).padding(8.dp)
                .clickable(onClick = {
                    viewModel.intentChannel.offer(
                        CharactersIntent.GetDetails(
                            characterId = item.id
                        )
                    )
                }),
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(8.dp)),
            border = BorderStroke(width = 0.5.dp, color = colorResource(id = R.color.gray3))
        ) {
            Column(modifier = Modifier.height(222.dp)) {
                AndroidView(
                    modifier = Modifier.height(140.dp).fillMaxWidth(),
                    viewBlock = {
                        AppCompatImageView(it).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            load(item.image)
                        }
                    },
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp),
                    style = MaterialTheme.typography.caption,
                    text = item.status
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    text = item.name
                )
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
