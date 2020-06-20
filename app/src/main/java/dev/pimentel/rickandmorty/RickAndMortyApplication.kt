package dev.pimentel.rickandmorty

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("Unused")
class RickAndMortyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RickAndMortyApplication)
            androidLogger(Level.DEBUG)
            loadKoinModules(appModules)
        }
    }
}
