package dev.pimentel.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersFragmentBinding
import dev.pimentel.rickandmorty.presentation.characters.filter.dto.CharactersFilter
import dev.pimentel.rickandmorty.shared.helpers.EndOfScrollListener
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CharactersFragment : Fragment(R.layout.characters_fragment) {

    private val binding by lifecycleBinding(CharactersFragmentBinding::bind)
    private val viewModel: CharactersContract.ViewModel by viewModel<CharactersViewModel>()
    private val adapter: CharactersAdapter by inject()

    private lateinit var endOfScrollListener: EndOfScrollListener<StaggeredGridLayoutManager>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(charactersModule)
        bindViewModelOutputs()
        bindViewModelInputs()
        bindResultListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(charactersModule)
        endOfScrollListener.dispose()
    }

    private fun bindViewModelOutputs() {
        val layoutManager = StaggeredGridLayoutManager(
            CHARACTERS_ROW_COUNT,
            RecyclerView.VERTICAL
        )
        endOfScrollListener = EndOfScrollListener(
            layoutManager,
            { false },
            { false },
            viewModel::getMoreCharacters
        )

        binding.apply {
            charactersList.also { list ->
                list.adapter = adapter
                list.layoutManager = layoutManager
                list.addOnScrollListener(endOfScrollListener)
            }

            viewModel.charactersState().observe(viewLifecycleOwner, Observer { state ->
                adapter.submitList(state.list)
            })
        }
    }

    private fun bindViewModelInputs() {
        binding.apply {
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.filter) viewModel.openFilters()
                return@setOnMenuItemClickListener true
            }
        }

        viewModel.getCharacters(CharactersFilter.BLANK)
    }

    private fun bindResultListener() {
        setFragmentResultListener(RESULT_LISTENER_KEY) { _, bundle ->
            val filter = bundle.get(CharactersFilter.RESULT_KEY) as CharactersFilter
            viewModel.getCharacters(filter)
        }
    }

    companion object {
        const val RESULT_LISTENER_KEY = "CHARACTERS_RESULT_LISTENER"

        private const val CHARACTERS_ROW_COUNT = 2
    }
}
