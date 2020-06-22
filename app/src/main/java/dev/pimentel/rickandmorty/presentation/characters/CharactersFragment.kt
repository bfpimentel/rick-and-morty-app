package dev.pimentel.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersFragmentBinding
import dev.pimentel.rickandmorty.presentation.characters.data.CharactersFilter
import dev.pimentel.rickandmorty.shared.helpers.EndOfScrollListener
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import timber.log.Timber

class CharactersFragment : Fragment(R.layout.characters_fragment) {

    private val binding by lifecycleBinding(CharactersFragmentBinding::bind)
    private val viewModel: CharactersContract.ViewModel by viewModel<CharactersViewModel>()
    private val adapter: CharactersAdapter by inject()

    private lateinit var endOfScrollListener: EndOfScrollListener<StaggeredGridLayoutManager>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(charactersModule)

        bindViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(charactersModule)
        endOfScrollListener.dispose()
    }

    private fun bindViewModel() {
        val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        endOfScrollListener = EndOfScrollListener(
            layoutManager,
            { false },
            { false },
            {
                Timber.d("Teste de scroll listener")
                viewModel.getCharacters(CharactersFilter.BLANK)
            }
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

        viewModel.getCharacters(
            CharactersFilter.BLANK
        )
    }
}
