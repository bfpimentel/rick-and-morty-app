package dev.pimentel.rickandmorty

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@Suppress("Unused")
@HiltAndroidApp
class RickAndMortyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
