package dev.pimentel.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersFragmentBinding
import dev.pimentel.rickandmorty.presentation.characters.filter.CharactersFilterFragment
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.shared.helpers.EndOfScrollListener
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.characters_fragment) {

    @Inject
    lateinit var adapter: CharactersAdapter

    private val binding by lifecycleBinding(CharactersFragmentBinding::bind)
    private val viewModel: CharactersContract.ViewModel by viewModels<CharactersViewModel>()

    private lateinit var endOfScrollListener: EndOfScrollListener<StaggeredGridLayoutManager>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindOutputs()
        bindInputs()
    }

    override fun onDestroy() {
        super.onDestroy()
        endOfScrollListener.dispose()
    }

    private fun bindOutputs() {
        binding.apply {
            viewModel.charactersState().observe(viewLifecycleOwner, Observer { state ->
                adapter.submitList(state.characters)
                state.scrollToTheTop?.also { charactersList.scrollToPosition(0) }
                state.errorMessage?.also {
                    errorContainer.visibility = View.VISIBLE
                    errorMessage.text = state.errorMessage
                    charactersList.visibility = View.GONE
                } ?: run {
                    errorContainer.visibility = View.GONE
                    charactersList.visibility = View.VISIBLE
                }
            })

            viewModel.error().observe(viewLifecycleOwner, Observer(::showErrorDialog))

            viewModel.filterIcon().observe(viewLifecycleOwner, Observer { icon ->
                toolbar.menu.findItem(R.id.filter).setIcon(icon)
            })
        }
    }

    private fun bindInputs() {
        val layoutManager = StaggeredGridLayoutManager(
            CHARACTERS_ROW_COUNT,
            RecyclerView.VERTICAL
        )

        endOfScrollListener = EndOfScrollListener(
            layoutManager,
            { false },
            { false },
            viewModel::getCharactersWithLastFilter
        )

        adapter.onItemClick = viewModel::getDetails

        binding.apply {
            charactersList.also { list ->
                list.adapter = adapter
                list.layoutManager = layoutManager
                list.addOnScrollListener(endOfScrollListener)
            }

            toolbar.menu.findItem(R.id.filter).setOnMenuItemClickListener {
                viewModel.openFilters()
                return@setOnMenuItemClickListener true
            }
        }

        parentFragmentManager.setFragmentResultListener(
            CharactersFilterFragment.CHARACTERS_RESULT_LISTENER_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            viewModel.getCharacters(
                bundle[CharactersFilterFragment.CHARACTERS_FILTER_RESULT_KEY] as CharactersFilter
            )
        }

        viewModel.getCharacters(CharactersFilter.NO_FILTER)
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
