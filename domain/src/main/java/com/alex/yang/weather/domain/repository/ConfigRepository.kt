package com.alex.yang.weather.domain.repository

import com.alex.yang.weather.domain.model.Counties

/**
 * Created by AlexYang on 2025/10/30.
 *
 *
 */
interface ConfigRepository {
    suspend fun fetchAndCache()

    suspend fun getCounties(): Counties
}