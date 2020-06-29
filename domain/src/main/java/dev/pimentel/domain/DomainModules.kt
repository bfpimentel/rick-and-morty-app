package dev.pimentel.domain

import dev.pimentel.domain.usecases.GetCharacterDetails
import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.domain.usecases.GetEpisodes
import dev.pimentel.domain.usecases.GetFilters
import dev.pimentel.domain.usecases.GetLocations
import dev.pimentel.domain.usecases.SaveFilter
import org.koin.dsl.module

private val useCaseModule = module {
    factory { GetCharacters(get()) }
    factory { GetLocations(get()) }
    factory { GetEpisodes(get()) }
    factory { GetFilters(get()) }
    factory { SaveFilter(get()) }
    factory { GetCharacterDetails(get()) }
}

val domainModules = listOf(
    useCaseModule
)
