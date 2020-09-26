package dev.pimentel.rickandmorty.shared.extensions

import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dev.pimentel.rickandmorty.R
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

fun Fragment.composeViewFor(content: @Composable () -> Unit): ComposeView {
    val lightColors = lightColors(
        primary = Color(color = 0x5856D6),
        primaryVariant = Color(color = 0x081F32),
    )

    val darkColors = darkColors(
        primary = Color(color = 0x5856D6),
        primaryVariant = Color(color = 0x081F32),
    )

    return ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme(
                colors = if (isSystemInDarkTheme()) darkColors else lightColors,
                typography = MaterialTheme.typography.copy(
                    h1 = MaterialTheme.typography.h1.copy(
                        fontFamily = fontFamily(font(R.font.roboto_regular))
                    ),
                    h2 = MaterialTheme.typography.h2.copy(
                        fontFamily = fontFamily(font(R.font.roboto_light))
                    ),
                    h3 = MaterialTheme.typography.h3.copy(
                        fontFamily = fontFamily(font(R.font.roboto_regular))
                    ),
                    h4 = MaterialTheme.typography.h4.copy(
                        fontFamily = fontFamily(font(R.font.roboto_regular))
                    ),
                    h5 = MaterialTheme.typography.h5.copy(
                        fontFamily = fontFamily(font(R.font.roboto_regular))
                    ),
                    h6 = MaterialTheme.typography.h6.copy(
                        fontFamily = fontFamily(font(R.font.roboto_medium))
                    ),
                    subtitle1 = MaterialTheme.typography.subtitle1.copy(
                        fontFamily = fontFamily(font(R.font.roboto_regular))
                    ),
                    subtitle2 = MaterialTheme.typography.subtitle2.copy(
                        fontFamily = fontFamily(font(R.font.roboto_medium))
                    ),
                    body1 = MaterialTheme.typography.body1.copy(
                        fontFamily = fontFamily(font(R.font.roboto_regular))
                    ),
                    body2 = MaterialTheme.typography.body2.copy(
                        fontFamily = fontFamily(font(R.font.roboto_regular))
                    ),
                    button = MaterialTheme.typography.button.copy(
                        fontFamily = fontFamily(font(R.font.roboto_medium))
                    ),
                    caption = MaterialTheme.typography.caption.copy(
                        fontFamily = fontFamily(font(R.font.roboto_regular))
                    ),
                    overline = MaterialTheme.typography.overline.copy(
                        fontFamily = fontFamily(font(R.font.roboto_medium))
                    ),
                )
            ) {
                content()
            }
        }
    }
}
