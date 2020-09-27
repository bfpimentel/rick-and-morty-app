package dev.pimentel.data.sources.local

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import dev.pimentel.data.dto.FilterDTO

interface FilterLocalDataSource {
    suspend fun saveFilter(filter: FilterDTO)
    suspend fun getFilters(type: FilterDTO.Type): List<FilterDTO>
}

class FilterLocalDataSourceImpl(
    private val sharedPreferences: SharedPreferences,
    private val jsonAdapter: JsonAdapter<List<FilterDTO>>
) : FilterLocalDataSource {

    private val allFilters: List<FilterDTO>
        get() {
            val filterListJson = sharedPreferences.getString(FILTER_KEY, null)!!
            return jsonAdapter.fromJson(filterListJson)!!
        }

    override suspend fun saveFilter(filter: FilterDTO) {
        val newFilterList: List<FilterDTO> =
            if (sharedPreferences.contains(FILTER_KEY)) {
                allFilters.toMutableList().apply {
                    remove(filter)
                    add(filter)
                    toList()
                }
            } else {
                listOf(filter)
            }

        sharedPreferences
            .edit()
            .putString(FILTER_KEY, jsonAdapter.toJson(newFilterList))
            .apply()
    }

    override suspend fun getFilters(type: FilterDTO.Type): List<FilterDTO> =
        if (sharedPreferences.contains(FILTER_KEY)) {
            allFilters.filter { filter -> filter.type == type }
        } else {
            listOf()
        }

    private companion object {
        const val FILTER_KEY = "FILTER"
    }
}
