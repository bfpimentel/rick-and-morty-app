package dev.pimentel.rickandmorty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.presentation.characters.CharactersScreen
import dev.pimentel.rickandmorty.presentation.characters.CharactersStore
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var charactersStore: CharactersStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        composeView { MainScreen() }
    }

    @Composable
    private fun MainScreen() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "characters",
        ) {
            composable("characters") { CharactersScreen(store = charactersStore) }
        }
    }
}

private fun AppCompatActivity.composeView(content: @Composable () -> Unit) {
    val lightColors = lightColors(
        primary = Color(color = 0x5856D6),
        primaryVariant = Color(color = 0x081F32),
    )

    val darkColors = darkColors(
        primary = Color(color = 0x5856D6),
        primaryVariant = Color(color = 0x081F32),
    )

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