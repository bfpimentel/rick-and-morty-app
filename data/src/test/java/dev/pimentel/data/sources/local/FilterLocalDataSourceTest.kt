package dev.pimentel.data.sources.local

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import dev.pimentel.data.models.FilterModel
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FilterLocalDataSourceTest {

    private val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
    private val jsonAdapter = mockk<JsonAdapter<List<FilterModel>>>(relaxed = true)
    private lateinit var filterLocalDataSource: FilterLocalDataSource

    @Test
    @BeforeEach
    fun `subject must not be null`() {
        filterLocalDataSource = FilterLocalDataSourceImpl(
            sharedPreferences,
            jsonAdapter
        )

        assertNotNull(filterLocalDataSource)
    }

    @Test
    fun `should just save filter when shared preferences does not contain the key`() {
        val filter = FilterModel("locationType1", FilterModel.Type.LOCATION_TYPE)
        val newFilterList = listOf(filter)
        val newFilterListJson = "json"

        every { sharedPreferences.contains(FILTER_KEY) } returns false
        every { jsonAdapter.toJson(newFilterList) } returns newFilterListJson
        every {
            sharedPreferences
                .edit()
                .putString(FILTER_KEY, newFilterListJson)
                .apply()
        } just runs

        filterLocalDataSource.saveFilter(filter)
            .test()
            .assertComplete()
            .assertNoErrors()

        verify(exactly = 1) {
            sharedPreferences.contains(FILTER_KEY)
            jsonAdapter.toJson(newFilterList)
            sharedPreferences
                .edit()
                .putString(FILTER_KEY, newFilterListJson)
                .apply()
        }
        confirmVerified(jsonAdapter, sharedPreferences)
    }

    @Test
    fun `should add filter and save when filter key exists and there are not equal old filters`() {
        val filter = FilterModel("locationType1", FilterModel.Type.LOCATION_TYPE)

        val oldFilterListJson = "oldJson"
        val oldFilterList = listOf(FilterModel("locationType2", FilterModel.Type.LOCATION_TYPE))

        val newFilterList = oldFilterList + filter
        val newFilterListJson = "newJson"

        every { sharedPreferences.contains(FILTER_KEY) } returns true
        every { sharedPreferences.getString(FILTER_KEY, null) } returns oldFilterListJson
        every { jsonAdapter.fromJson(oldFilterListJson) } returns oldFilterList
        every { jsonAdapter.toJson(newFilterList) } returns newFilterListJson
        every {
            sharedPreferences
                .edit()
                .putString(FILTER_KEY, newFilterListJson)
                .apply()
        } just runs

        filterLocalDataSource.saveFilter(filter)
            .test()
            .assertComplete()
            .assertNoErrors()

        verify(exactly = 1) {
            sharedPreferences.contains(FILTER_KEY)
            sharedPreferences.getString(FILTER_KEY, null)
            jsonAdapter.fromJson(oldFilterListJson)
            jsonAdapter.toJson(newFilterList)
            sharedPreferences
                .edit()
                .putString(FILTER_KEY, newFilterListJson)
                .apply()
        }
        confirmVerified(jsonAdapter, sharedPreferences)
    }

    @Test
    fun `should remove and add filter and save when filter key exists and there are equal old filters`() {
        val filter = FilterModel("locationType1", FilterModel.Type.LOCATION_TYPE)

        val oldFilterListJson = "oldJson"
        val oldFilterList = listOf(
            FilterModel("locationType1", FilterModel.Type.LOCATION_TYPE),
            FilterModel("locationType2", FilterModel.Type.LOCATION_TYPE)
        )

        val newFilterList = listOf(
            FilterModel("locationType2", FilterModel.Type.LOCATION_TYPE),
            FilterModel("locationType1", FilterModel.Type.LOCATION_TYPE)
        )
        val newFilterListJson = "newJson"

        every { sharedPreferences.contains(FILTER_KEY) } returns true
        every { sharedPreferences.getString(FILTER_KEY, null) } returns oldFilterListJson
        every { jsonAdapter.fromJson(oldFilterListJson) } returns oldFilterList
        every { jsonAdapter.toJson(newFilterList) } returns newFilterListJson
        every {
            sharedPreferences
                .edit()
                .putString(FILTER_KEY, newFilterListJson)
                .apply()
        } just runs

        filterLocalDataSource.saveFilter(filter)
            .test()
            .assertComplete()
            .assertNoErrors()

        verify(exactly = 1) {
            sharedPreferences.contains(FILTER_KEY)
            sharedPreferences.getString(FILTER_KEY, null)
            jsonAdapter.fromJson(oldFilterListJson)
            jsonAdapter.toJson(newFilterList)
            sharedPreferences
                .edit()
                .putString(FILTER_KEY, newFilterListJson)
                .apply()
        }
        confirmVerified(jsonAdapter, sharedPreferences)
    }

    @Test
    fun `should just return empty list when key does not exist`() {
        val type = FilterModel.Type.LOCATION_TYPE

        every { sharedPreferences.contains(FILTER_KEY) } returns false

        filterLocalDataSource.getFilters(type)
            .test()
            .assertResult(listOf())
            .assertNoErrors()

        verify(exactly = 1) {
            sharedPreferences.contains(FILTER_KEY)
        }
        confirmVerified(jsonAdapter, sharedPreferences)
    }

    @Test
    fun `should just return shared preferences filtered list when key exists`() {
        val type = FilterModel.Type.LOCATION_TYPE

        val listJson = "listJson"
        val allTypesList = listOf(
            FilterModel("locationType2", FilterModel.Type.LOCATION_TYPE),
            FilterModel("locationType1", FilterModel.Type.LOCATION_DIMENSION)
        )

        val filteredList = listOf(
            FilterModel("locationType2", FilterModel.Type.LOCATION_TYPE)
        )

        every { sharedPreferences.contains(FILTER_KEY) } returns true
        every { sharedPreferences.getString(FILTER_KEY, null) } returns listJson
        every { jsonAdapter.fromJson(listJson) } returns allTypesList

        filterLocalDataSource.getFilters(type)
            .test()
            .assertResult(filteredList)
            .assertNoErrors()

        verify(exactly = 1) {
            sharedPreferences.contains(FILTER_KEY)
            sharedPreferences.getString(FILTER_KEY, null)
            jsonAdapter.fromJson(listJson)
        }
        confirmVerified(jsonAdapter, sharedPreferences)
    }

    private companion object {
        const val FILTER_KEY = "FILTER"
    }
}
