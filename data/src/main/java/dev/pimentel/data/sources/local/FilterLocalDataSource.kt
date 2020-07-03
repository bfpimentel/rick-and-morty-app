package dev.pimentel.data.sources.local

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import dev.pimentel.data.models.FilterModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface FilterLocalDataSource {
    fun saveFilter(filter: FilterModel): Completable
    fun getFilters(type: FilterModel.Type): Single<List<FilterModel>>
}

class FilterLocalDataSourceImpl(
    private val sharedPreferences: SharedPreferences,
    private val jsonAdapter: JsonAdapter<List<FilterModel>>
) : FilterLocalDataSource {

    private val allFilters: List<FilterModel>
        get() {
            val filterListJson = sharedPreferences.getString(FILTER_KEY, null)!!
            return jsonAdapter.fromJson(filterListJson)!!
        }

    override fun saveFilter(filter: FilterModel): Completable =
        Completable.fromAction {
            val newFilterList: List<FilterModel> =
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

    override fun getFilters(type: FilterModel.Type): Single<List<FilterModel>> =
        Single.fromCallable {
            if (sharedPreferences.contains(FILTER_KEY)) {
                allFilters.filter { filter -> filter.type == type }
            } else {
                listOf()
            }
        }

    private companion object {
        const val FILTER_KEY = "FILTER"
    }
}
