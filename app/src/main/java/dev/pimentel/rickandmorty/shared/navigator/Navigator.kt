package dev.pimentel.rickandmorty.shared.navigator

import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import dev.pimentel.rickandmorty.shared.dispatchersprovider.DispatchersProvider
import kotlinx.coroutines.launch

interface NavigatorBinder {
    fun bind(navController: NavController, lifecycleScope: LifecycleCoroutineScope)
    fun unbind()
}

interface NavigatorRouter {
    fun navigate(@IdRes destinationId: Int)
    fun <T> navigate(@IdRes destinationId: Int, argument: Pair<String, T>)
    fun pop()
}

interface Navigator : NavigatorBinder, NavigatorRouter

class NavigatorImpl(private val dispatchersProvider: DispatchersProvider) : Navigator {

    private var navController: NavController? = null
    private var lifecycleScope: LifecycleCoroutineScope? = null

    override fun bind(navController: NavController, lifecycleScope: LifecycleCoroutineScope) {
        this.navController = navController
        this.lifecycleScope = lifecycleScope
    }

    override fun unbind() {
        this.navController = null
        this.lifecycleScope = null
    }

    override fun navigate(@IdRes destinationId: Int) {
        lifecycleScope?.launch(dispatchersProvider.ui) {
            navController?.navigate(destinationId)
        }
    }

    override fun <T> navigate(destinationId: Int, argument: Pair<String, T>) {
        lifecycleScope?.launch(dispatchersProvider.ui) {
            navController?.navigate(destinationId, bundleOf(argument))
        }
    }

    override fun pop() {
        lifecycleScope?.launch(dispatchersProvider.ui) {
            navController?.popBackStack()
        }
    }
}
