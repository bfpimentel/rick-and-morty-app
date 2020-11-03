package dev.pimentel.rickandmorty

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.pimentel.rickandmorty.presentation.characters.CharactersScreen
import dev.pimentel.rickandmorty.presentation.characters.CharactersStore
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var charactersStore: CharactersStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        composeView { MainScreen() }
    }

    @Composable
    private fun MainScreen() {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigation(backgroundColor = Color.White) {
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = backStackEntry?.arguments?.getString(KEY_ROUTE)

                    val mainNavigationItems = listOf(
                        Route.Characters,
                        Route.Locations,
                        Route.Episodes,
                    )

                    mainNavigationItems.forEach { item ->
                        val isSelected = currentRoute == item.route

                        BottomNavigationItem(
                            label = { Text(text = stringResource(id = item.labelRes)) },
                            icon = { Icon(asset = vectorResource(id = item.icon(isSelected))) },
                            selected = isSelected,
                            onClick = {
                                navController.popBackStack(
                                    navController.graph.startDestination,
                                    false
                                )

                                if (currentRoute != item.route) {
                                    navController.navigate(item.route)
                                }
                            }
                        )
                    }
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = Route.Characters.route,
            ) {
                composable(Route.Characters.route) { CharactersScreen(store = charactersStore) }
                composable(Route.Episodes.route) { Text(text = Route.Episodes.route) }
                composable(Route.Locations.route) { Text(text = Route.Locations.route) }
            }
        }
    }
}

sealed class Route(
    val route: String,
    @StringRes val labelRes: Int,
    @DrawableRes private val defaultIconRes: Int,
    @DrawableRes private val selectedIconRes: Int,
) {
    object Characters : Route(
        route = "characters",
        labelRes = R.string.bottom_characters,
        defaultIconRes = R.drawable.ic_characters_default,
        selectedIconRes = R.drawable.ic_characters_selected
    )

    object Locations : Route(
        route = "locations",
        labelRes = R.string.bottom_locations,
        defaultIconRes = R.drawable.ic_locations_default,
        selectedIconRes = R.drawable.ic_locations_selected,
    )

    object Episodes : Route(
        route = "episodes",
        labelRes = R.string.bottom_episodes,
        defaultIconRes = R.drawable.ic_episodes_default,
        selectedIconRes = R.drawable.ic_episodes_selected
    )

    fun icon(isSelected: Boolean) = if (isSelected) selectedIconRes else defaultIconRes
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