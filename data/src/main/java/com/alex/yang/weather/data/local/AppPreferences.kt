package com.alex.yang.weather.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alex.yang.weather.data.model.County
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
@Singleton
class AppPreferences @Inject constructor(
    private val context: Context,
    private val json: Json,
) {
    private val Context.dataStore by preferencesDataStore(name = "com.alex.yang.weather.demo.me")

    suspend fun saveConfig(
        apiKey: String,
        countiesJson: String
    ) {
        context.dataStore.edit {
            it[Keys.KEY_API_KEY] = apiKey
            it[Keys.KEY_COUNTIES_JSON] = countiesJson
        }

        Log.i("DEBUG", "[DEBUG] saveConfig Success")
    }

    fun getApiKey(): Flow<String> {
        return context.dataStore.data
            .map { it[Keys.KEY_API_KEY] ?: "W4GXC3K9XX7JV9DJ99BCS5Z69" }
    }

    fun getCountiesJson(): Flow<String> =
        context.dataStore.data
            .map { it[Keys.KEY_COUNTIES_JSON].orEmpty() }

    // 儲存使用者選擇的城市（序列化 County -> JSON）
    suspend fun saveSelectedCounty(county: County) {
        context.dataStore.edit {
            it[Keys.KEY_SELECTED_COUNTY] = json.encodeToString(county)
        }
        Log.i("DEBUG", "[DEBUG] saveSelectedCounty: ${county.zh} / ${county.en}")
    }

    // 讀取使用者選擇的城市（反序列化 JSON -> County）
    fun getSelectedCounty(): Flow<County> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.KEY_SELECTED_COUNTY]
                ?.let { runCatching { json.decodeFromString<County>(it) }.getOrNull() }
                ?: County.DEFAULT
        }

    private object Keys {
        val KEY_API_KEY = stringPreferencesKey("key_api_key")
        val KEY_COUNTIES_JSON = stringPreferencesKey("key_tw_counties_v1_json")
        val KEY_SELECTED_COUNTY = stringPreferencesKey("key_city")
    }
}