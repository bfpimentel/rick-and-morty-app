package dev.pimentel.data

import dev.pimentel.data.repositories.CharactersRepositoryImpl
import dev.pimentel.domain.repositories.CharactersRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val REQUEST_TIMEOUT = 60L

private val networkModule = module {
    single {
        val apiUrl = androidContext().getString(R.string.api_url)

        OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
            .let { client ->
                Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
            }
    }
}

private val sourceModule = module {
    single { get<Retrofit>().create(CharactersRepository::class.java) }
}

private val repositoryModule = module {
    single<CharactersRepository> { CharactersRepositoryImpl(get()) }
}

val dataModules = listOf(
    networkModule,
    sourceModule,
    repositoryModule
)
