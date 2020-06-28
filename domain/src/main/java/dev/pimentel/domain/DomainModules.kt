package dev.pimentel.domain

import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.domain.usecases.GetFilters
import dev.pimentel.domain.usecases.GetLocations
import dev.pimentel.domain.usecases.SaveFilter
import org.koin.dsl.module

private val useCaseModule = module {
    factory { GetCharacters(get()) }
    factory { GetLocations(get()) }
    factory { GetFilters(get()) }
    factory { SaveFilter(get()) }
}

val domainModules = listOf(
    useCaseModule
)
