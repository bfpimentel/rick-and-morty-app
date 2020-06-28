package dev.pimentel.rickandmorty.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pimentel.rickandmorty.presentation.filter.dto.FilterType

class FilterViewModel : ViewModel(), FilterContract.ViewModel {

    private val titleRes = MutableLiveData<Int>()
    private val filters = MutableLiveData<List<String>>()

    override fun title(): LiveData<Int> = titleRes
    override fun filters(): LiveData<List<String>> = filters

    override fun initializeWithFilterType(filterType: FilterType) {
        titleRes.postValue(filterType.nameRes)
    }
}
