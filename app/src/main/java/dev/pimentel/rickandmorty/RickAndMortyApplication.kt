package dev.pimentel.rickandmorty

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("Unused")
class RickAndMortyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RickAndMortyApplication)
            loadKoinModules(appModules)
        }
        Timber.plant(Timber.DebugTree())
    }
}
