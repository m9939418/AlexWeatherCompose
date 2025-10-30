package com.alex.yang.weather.domain.repository

import com.alex.yang.weather.domain.model.County
import kotlinx.coroutines.flow.Flow

/**
 * Created by AlexYang on 2025/10/30.
 *
 *
 */
interface DataStoreRepository {
    suspend fun saveConfig(apiKey: String, countiesJson: String)

    fun getApiKey(): Flow<String>

    fun getCountiesJson(): Flow<String>

    suspend fun saveSelectedCounty(county: County)

    fun getSelectedCounty(): Flow<County>
}