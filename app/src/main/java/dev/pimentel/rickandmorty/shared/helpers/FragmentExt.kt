package dev.pimentel.rickandmorty.shared.helpers

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> Fragment.lifecycleBinding(bindingFactory: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

        private var binding: T? = null

        init {
            this@lifecycleBinding
                .viewLifecycleOwnerLiveData
                .observe(this@lifecycleBinding, { owner ->
                    owner?.lifecycle?.addObserver(this)
                })
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            return binding ?: bindingFactory(requireView()).also { newBinding ->
                binding = newBinding
            }
        }
    }

fun Fragment.composeViewFor(content: @Composable () -> Unit) =
    ComposeView(requireContext()).apply {
        setContent(content)
    }
