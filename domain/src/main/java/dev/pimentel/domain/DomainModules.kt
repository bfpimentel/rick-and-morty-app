package dev.pimentel.domain

import dev.pimentel.domain.usecases.GetCharacters
import org.koin.core.module.Module
import org.koin.dsl.module

private val useCaseModule = module {
    factory { GetCharacters(get()) }
}

val domainModules = listOf<Module>(
    useCaseModule
)
