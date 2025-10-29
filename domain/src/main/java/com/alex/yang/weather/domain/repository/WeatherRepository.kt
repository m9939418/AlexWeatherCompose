package com.alex.yang.weather.domain.repository

import com.alex.yang.weather.core.network.Resource
import com.alex.yang.weather.domain.model.Timeline

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
interface WeatherRepository {
    suspend fun getWeather(
        location: String = "Taipei,TW",
        startDay: String,
        endDay: String,
        apiKey: String
    ): Resource<Timeline>
}