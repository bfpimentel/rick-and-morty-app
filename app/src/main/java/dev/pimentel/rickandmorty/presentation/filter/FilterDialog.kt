package dev.pimentel.rickandmorty.presentation.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.FilterDialogBinding
import dev.pimentel.rickandmorty.shared.helpers.lifecycleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class FilterDialog : DialogFragment(R.layout.filter_dialog) {

    private val binding by lifecycleBinding(FilterDialogBinding::bind)
    private val viewModel: FilterContract.ViewModel by viewModel<FilterViewModel>()

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
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(filterModule)
    }
}
