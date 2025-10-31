package com.alex.yang.home.util

import com.alex.yang.weather.core.network.Resource
import com.alex.yang.weather.domain.model.Timeline
import com.alex.yang.weather.domain.repository.WeatherRepository

/**
 * Created by AlexYang on 2025/10/31.
 *
 *
 */
class MockWeatherRepository : WeatherRepository {
    var nextResult: Resource<Timeline>? = null

    override suspend fun getWeather(
        location: String,
        startDay: String,
        endDay: String,
        apiKey: String
    ): Resource<Timeline> = checkNotNull(nextResult) { "FakeWeatherRepository.nextResult 尚未設定" }
}