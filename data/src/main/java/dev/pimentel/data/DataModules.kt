package dev.pimentel.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.pimentel.data.models.FilterModel
import dev.pimentel.data.repositories.CharacterNamesRepositoryImpl
import dev.pimentel.data.repositories.CharactersRepositoryImpl
import dev.pimentel.data.sources.local.FilterLocalDataSource
import dev.pimentel.data.sources.local.FilterLocalDataSourceImpl
import dev.pimentel.data.sources.remote.CharactersRemoteDataSource
import dev.pimentel.domain.repositories.CharacterNamesRepository
import dev.pimentel.domain.repositories.CharactersRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val REQUEST_TIMEOUT = 60L

private val moshiModule = module {
    Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

private val networkModule = module {
    single {
        val apiUrl = androidContext().getString(R.string.api_url)

        val client = OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return@single Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}

private val remoteDataSourceModule = module {
    single { get<Retrofit>().create(CharactersRemoteDataSource::class.java) }
}

private val sharedPreferencesModule = module {
    single {
        androidContext().getSharedPreferences(
            androidContext().packageName,
            Context.MODE_PRIVATE
        )
    }
}

private val localDataSourceModule = module {
    single<FilterLocalDataSource> {
        val filterListType = Types.newParameterizedType(List::class.java, FilterModel::class.java)

        FilterLocalDataSourceImpl(
            get(),
            get<Moshi>().adapter(filterListType)
        )
    }
}

private val repositoryModule = module {
    single<CharactersRepository> { CharactersRepositoryImpl(get()) }
    single<CharacterNamesRepository> { CharacterNamesRepositoryImpl(get()) }
}

val dataModules = listOf(
    moshiModule,
    networkModule,
    remoteDataSourceModule,
    sharedPreferencesModule,
    localDataSourceModule,
    repositoryModule
)
