package dev.pimentel.data.di

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.pimentel.data.R
import dev.pimentel.data.dto.FilterDTO
import dev.pimentel.data.repositories.characters.CharactersRepositoryImpl
import dev.pimentel.data.repositories.episodes.EpisodesRepositoryImpl
import dev.pimentel.data.repositories.filter.FilterRepositoryImpl
import dev.pimentel.data.repositories.filter.FilterTypeModelMapper
import dev.pimentel.data.repositories.filter.FilterTypeModelMapperImpl
import dev.pimentel.data.repositories.locations.LocationsRepositoryImpl
import dev.pimentel.data.sources.local.FilterLocalDataSource
import dev.pimentel.data.sources.local.FilterLocalDataSourceImpl
import dev.pimentel.data.sources.remote.CharactersRemoteDataSource
import dev.pimentel.data.sources.remote.EpisodesRemoteDataSource
import dev.pimentel.data.sources.remote.LocationsRemoteDataSource
import dev.pimentel.domain.repositories.CharactersRepository
import dev.pimentel.domain.repositories.EpisodesRepository
import dev.pimentel.domain.repositories.FilterRepository
import dev.pimentel.domain.repositories.LocationsRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Suppress("TooManyFunctions")
@Module
@InstallIn(ApplicationComponent::class)
object DataModules {

    private const val REQUEST_TIMEOUT = 60L

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(
        moshi: Moshi,
        @ApplicationContext context: Context
    ): Retrofit {
        val apiUrl = context.getString(R.string.api_url)

        val client = OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun providesCharactersRemoteDataSource(retrofit: Retrofit): CharactersRemoteDataSource =
        retrofit.create(CharactersRemoteDataSource::class.java)

    @Provides
    @Singleton
    fun providesLocationsRemoteDataSource(retrofit: Retrofit): LocationsRemoteDataSource =
        retrofit.create(LocationsRemoteDataSource::class.java)

    @Provides
    @Singleton
    fun providesEpisodesRemoteDataSource(retrofit: Retrofit): EpisodesRemoteDataSource =
        retrofit.create(EpisodesRemoteDataSource::class.java)

    @Provides
    @Singleton
    fun providesSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(
        context.packageName,
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun providesFilterLocalDataSource(
        sharedPreferences: SharedPreferences,
        moshi: Moshi
    ): FilterLocalDataSource {
        val filterListType = Types.newParameterizedType(List::class.java, FilterDTO::class.java)

        return FilterLocalDataSourceImpl(
            sharedPreferences,
            moshi.adapter(filterListType)
        )
    }

    @Provides
    @Singleton
    fun providesCharactersRepository(
        charactersRemoteDataSource: CharactersRemoteDataSource,
        episodesRemoteDataSource: EpisodesRemoteDataSource
    ): CharactersRepository = CharactersRepositoryImpl(
        charactersRemoteDataSource,
        episodesRemoteDataSource
    )

    @Provides
    @Singleton
    fun providesLocationsRepository(
        locationsRemoteDataSource: LocationsRemoteDataSource
    ): LocationsRepository = LocationsRepositoryImpl(locationsRemoteDataSource)

    @Provides
    @Singleton
    fun providesEpisodesRepository(
        episodesRemoteDataSource: EpisodesRemoteDataSource
    ): EpisodesRepository = EpisodesRepositoryImpl(episodesRemoteDataSource)

    @Provides
    @Singleton
    fun providesFilterTypeModelMapper(): FilterTypeModelMapper = FilterTypeModelMapperImpl()

    @Provides
    @Singleton
    fun providesFilterRepository(
        filterLocalDataSource: FilterLocalDataSource,
        filterTypeModelMapper: FilterTypeModelMapper
    ): FilterRepository = FilterRepositoryImpl(
        filterLocalDataSource,
        filterTypeModelMapper
    )
}
