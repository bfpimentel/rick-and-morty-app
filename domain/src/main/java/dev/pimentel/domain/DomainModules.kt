package dev.pimentel.domain

import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.domain.usecases.GetFilters
import org.koin.dsl.module

private val useCaseModule = module {
    factory { GetCharacters(get()) }
    factory { GetFilters(get()) }
}

val domainModules = listOf(
    useCaseModule
)
