package dev.pimentel.rickandmorty.shared.navigator

import androidx.annotation.IdRes
import androidx.navigation.NavController

interface NavigatorBinder {
    fun bind(navController: NavController)
    fun unbind()
}

interface NavigatorRouter {
    fun navigate(@IdRes destinationId: Int)
    fun pop()
}

interface Navigator : NavigatorBinder, NavigatorRouter

class NavigatorImpl : Navigator {

    private var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        this.navController = null
    }

    override fun navigate(@IdRes destinationId: Int) {
        navController?.navigate(destinationId)
    }

    override fun pop() {
        navController?.popBackStack()
    }
}
