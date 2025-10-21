package com.alex.yang.weather.domain.usecase

import com.alex.yang.weather.core.network.Resource
import com.alex.yang.weather.domain.model.Timeline
import com.alex.yang.weather.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/17.
 *
 *
 */
class GetWeatherUseCase @Inject constructor(
    val repository: WeatherRepository
) {
    suspend operator fun invoke(
        location: String,
        startDay: String,
        endDay: String,
        apiKey: String
    ): Resource<Timeline> = repository.getWeather(location, startDay, endDay, apiKey)
}