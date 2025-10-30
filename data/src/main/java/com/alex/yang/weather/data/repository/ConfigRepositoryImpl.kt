package com.alex.yang.weather.data.repository

import com.alex.yang.weather.data.datasource.AssetsLocalDataSource
import com.alex.yang.weather.data.datasource.RemoteConfigDataSource
import com.alex.yang.weather.domain.model.Counties
import com.alex.yang.weather.domain.repository.ConfigRepository
import com.alex.yang.weather.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/30.
 *
 *
 */
class ConfigRepositoryImpl @Inject constructor(
    private val remote: RemoteConfigDataSource,
    private val assets: AssetsLocalDataSource,
    private val prefs: DataStoreRepository,
    private val json: Json
) : ConfigRepository {
    override suspend fun fetchAndCache() {
        runCatching {
            remote.fetchAndActivate()

            val apiKey = remote.getString(KEY_API_KEY)
                .ifBlank { prefs.getApiKey().first() }

            val countiesJson = remote.getString(KEY_TW_COUNTIES_V1)
                .ifBlank { assets.readJson() }

            prefs.saveConfig(apiKey = apiKey, countiesJson = countiesJson)
        }.onFailure {
            val fallbackApiKey = prefs.getApiKey().first()
            val fallbackCounties = assets.readJson()
            prefs.saveConfig(apiKey = fallbackApiKey, countiesJson = fallbackCounties)
        }
    }

    override suspend fun getCounties(): Counties {
        val text = prefs.getCountiesJson().first().ifBlank { assets.readJson() }
        return json.decodeFromString(text)
    }

    companion object {
        const val KEY_API_KEY = "api_key"
        const val KEY_TW_COUNTIES_V1 = "tw_counties_v1"
    }
}