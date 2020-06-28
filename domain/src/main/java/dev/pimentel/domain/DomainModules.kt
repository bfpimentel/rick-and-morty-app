package dev.pimentel.domain

import dev.pimentel.domain.usecases.*
import org.koin.dsl.module

private val useCaseModule = module {
    factory { GetCharacters(get()) }
    factory { GetLocations(get()) }
    factory { GetEpisodes(get()) }
    factory { GetFilters(get()) }
    factory { SaveFilter(get()) }
}

val domainModules = listOf(
    useCaseModule
)
