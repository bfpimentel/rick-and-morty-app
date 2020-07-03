package dev.pimentel.rickandmorty.presentation.filter

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapper
import dev.pimentel.rickandmorty.presentation.filter.mappers.FilterTypeMapperImpl

@Module
@InstallIn(FragmentComponent::class)
object FilterModule {

//    @Provides
//    @FragmentScoped
//    fun providesFilterTypeMapper(): FilterTypeMapper = FilterTypeMapperImpl()
}
