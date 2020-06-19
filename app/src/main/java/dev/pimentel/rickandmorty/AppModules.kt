package dev.pimentel.rickandmorty

import dev.pimentel.data.dataModules
import dev.pimentel.domain.domainModules
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.shared.navigator.NavigatorImpl
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val schedulerProviderModule = module {
    single<SchedulerProvider> { SchedulerProviderImpl() }
}

private val navigatorModule = module {
    single<Navigator> { NavigatorImpl() }
}

private val errorHandlingModule = module {
    single { GetErrorMessage(androidContext()) }
}

val appModules = listOf(
    schedulerProviderModule,
    navigatorModule,
    errorHandlingModule
) + domainModules + dataModules
