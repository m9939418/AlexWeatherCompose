package com.alex.yang.weather.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alex.yang.weather.domain.model.County
import com.alex.yang.weather.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/30.
 *
 *
 */
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : DataStoreRepository {
    override suspend fun saveConfig(apiKey: String, countiesJson: String) {
        dataStore.edit { pref ->
            pref[KEY_API_KEY] = apiKey
            pref[KEY_COUNTIES_JSON] = countiesJson
        }
    }

    override fun getApiKey(): Flow<String> =
        dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { it[KEY_API_KEY] ?: DEFAULT_API_KEY }

    override fun getCountiesJson(): Flow<String> =
        dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { it[KEY_COUNTIES_JSON] ?: "" }

    override suspend fun saveSelectedCounty(county: County) {
        val text = json.encodeToString(county)
        dataStore.edit { pref -> pref[KEY_SELECTED_COUNTY] = text }
    }

    override fun getSelectedCounty(): Flow<County> =
        dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { pref ->
                pref[KEY_SELECTED_COUNTY]
                    ?.let { runCatching { json.decodeFromString<County>(it) }.getOrNull() }
                    ?: County.DEFAULT
            }

    companion object {
        private val KEY_API_KEY = stringPreferencesKey("key_api_key")
        private val KEY_COUNTIES_JSON = stringPreferencesKey("key_tw_counties_v1_json")
        private val KEY_SELECTED_COUNTY = stringPreferencesKey("key_city")

        private const val DEFAULT_API_KEY = "W4GXC3K9XX7JV9DJ99BCS5Z69"
    }
}