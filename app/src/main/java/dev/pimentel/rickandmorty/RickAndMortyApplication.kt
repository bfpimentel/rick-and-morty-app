package dev.pimentel.rickandmorty

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

@Suppress("Unused")
class RickAndMortyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RickAndMortyApplication)
            loadKoinModules(appModules)
        }
    }
}
