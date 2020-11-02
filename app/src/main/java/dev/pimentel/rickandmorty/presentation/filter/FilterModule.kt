package dev.pimentel.rickandmorty.presentation.filter

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FilterModule {

//    @Provides
//    @FragmentScoped
//    fun providesFilterTypeMapper(): FilterTypeMapper = FilterTypeMapperImpl()
}
