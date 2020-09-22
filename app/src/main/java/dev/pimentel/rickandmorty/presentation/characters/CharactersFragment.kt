package dev.pimentel.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.ColumnScope.weight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.ui.tooling.preview.Preview
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersItemBinding
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersState
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.shared.views.LazyVerticalGridForIndexed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

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
    ): View = ComposeView(requireContext()).apply { setContent { Screen() } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        bindOutputs()
//        bindInputs()
        viewModel.getCharacters(CharactersFilter.NO_FILTER)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        endOfScrollListener.dispose()
//    }

    @Preview(name = "Screen")
    @Composable
    private fun Screen() {
        val charactersState = viewModel.charactersState().collectAsState()

        MaterialTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Characters") }
                    )
                }
            ) {
                when (val value = charactersState.value) {
                    is CharactersState.Success -> SuccessScreen(value = value)
                    else -> {

                    }
                }
            }
        }
    }

    @Composable
    private fun SuccessScreen(value: CharactersState.Success) {
        LazyVerticalGridForIndexed(
            items = value.characters,
            perRow = 2
        ) { index, character ->
            AndroidView(
                modifier = Modifier.weight(1f).padding(8.dp),
                viewBlock = {
                    CharactersItemBinding.inflate(
                        LayoutInflater.from(it)
                    ).apply {
                        root.setOnClickListener { viewModel.getDetails(character.id) }

                        image.load(character.image)
                        status.text = character.status
                        name.text = character.name
                    }.root
                })

            if (index == value.characters.lastIndex) {
                viewModel.getCharactersWithLastFilter()
            }
        }
    }

//    private fun bindOutputs() {
//        binding.apply {
//            viewModel.charactersState().observe(viewLifecycleOwner, Observer { state ->
//                adapter.submitList(state.characters)
//                state.scrollToTheTop?.also { charactersList.scrollToPosition(0) }
//                state.errorMessage?.also {
//                    errorContainer.visibility = View.VISIBLE
//                    errorMessage.text = state.errorMessage
//                    charactersList.visibility = View.GONE
//                } ?: run {
//                    errorContainer.visibility = View.GONE
//                    charactersList.visibility = View.VISIBLE
//                }
//            })
//
//            viewModel.error().observe(viewLifecycleOwner, Observer(::showErrorDialog))
//
//            viewModel.filterIcon().observe(viewLifecycleOwner, Observer { icon ->
//                toolbar.menu.findItem(R.id.filter).setIcon(icon)
//            })
//        }
//    }
//
//    private fun bindInputs() {
//        val layoutManager = StaggeredGridLayoutManager(
//            CHARACTERS_ROW_COUNT,
//            RecyclerView.VERTICAL
//        )
//
//        endOfScrollListener = EndOfScrollListener(
//            layoutManager,
//            { false },
//            { false },
//            viewModel::getCharactersWithLastFilter
//        )
//
//        adapter.onItemClick = viewModel::getDetails
//
//        binding.apply {
//            charactersList.also { list ->
//                list.adapter = adapter
//                list.layoutManager = layoutManager
//                list.addOnScrollListener(endOfScrollListener)
//            }
//
//            toolbar.menu.findItem(R.id.filter).setOnMenuItemClickListener {
//                viewModel.openFilters()
//                return@setOnMenuItemClickListener true
//            }
//        }
//
//        parentFragmentManager.setFragmentResultListener(
//            CharactersFilterFragment.CHARACTERS_RESULT_LISTENER_KEY,
//            viewLifecycleOwner
//        ) { _, bundle ->
//            viewModel.getCharacters(
//                bundle[CharactersFilterFragment.CHARACTERS_FILTER_RESULT_KEY] as CharactersFilter
//            )
//        }
//
//        viewModel.getCharacters(CharactersFilter.NO_FILTER)
//    }
//
//    private fun showErrorDialog(errorMessage: String) {
//        AlertDialog.Builder(requireContext())
//            .setTitle(R.string.error_dialog_title)
//            .setMessage(errorMessage)
//            .create()
//            .show()
//    }

    private companion object {
        const val CHARACTERS_ROW_COUNT = 2
    }
}
