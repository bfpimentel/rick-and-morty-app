package dev.pimentel.domain

import dev.pimentel.domain.usecases.GetCharacters
import dev.pimentel.domain.usecases.TestGet
import org.koin.dsl.module

private val useCaseModule = module {
    factory { GetCharacters(get()) }
    factory { TestGet(get()) }
}

val domainModules = listOf(
    useCaseModule
)
