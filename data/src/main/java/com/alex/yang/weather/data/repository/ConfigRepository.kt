package com.alex.yang.weather.data.repository

import android.content.Context
import android.util.Log
import com.alex.yang.weather.data.local.AppPreferences
import com.alex.yang.weather.data.model.Counties
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
@Singleton
class ConfigRepository @Inject constructor(
    val context: Context,
    val remoteConfig: FirebaseRemoteConfig,
    val preferences: AppPreferences,
    val json: Json
) {
    suspend fun fetchConfig() {
        remoteConfig.fetchAndActivate().await()

        val apiKey = remoteConfig.getString(KEY_API_KEY)
        val countiesJson = remoteConfig.getString(KEY_TW_COUNTIES_V1).ifBlank { getCountiesJson() }

        preferences.saveConfig(apiKey = apiKey, countiesJson = countiesJson)
    }

    suspend fun getCounties(): Counties {
        val data = preferences.getCountiesJson().first()
        return json.decodeFromString(Counties.serializer(), data)
    }

    private fun getCountiesJson(): String {
        return context.assets.open("tw_counties.json").use {
            Log.i("DEBUG", "[DEBUG] 讀取 assets tw_counties.json")
            it.readBytes().toString(Charset.forName("UTF-8"))
        }
    }

    companion object {
        const val KEY_API_KEY = "api_key"
        const val KEY_TW_COUNTIES_V1 = "tw_counties_v1"
    }
}