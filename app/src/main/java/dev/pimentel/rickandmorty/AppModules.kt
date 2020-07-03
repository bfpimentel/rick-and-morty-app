package dev.pimentel.rickandmorty

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.pimentel.rickandmorty.shared.errorhandling.GetErrorMessage
import dev.pimentel.rickandmorty.shared.navigator.Navigator
import dev.pimentel.rickandmorty.shared.navigator.NavigatorImpl
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProvider
import dev.pimentel.rickandmorty.shared.schedulerprovider.SchedulerProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

    @Provides
    @Singleton
    fun provideNavigator(): Navigator = NavigatorImpl()

    @Provides
    fun provideGetErrorMessage(
        @ApplicationContext context: Context
    ): GetErrorMessage = GetErrorMessage(context)
}
