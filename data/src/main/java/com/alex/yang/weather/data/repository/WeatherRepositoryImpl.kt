package com.alex.yang.weather.data.repository

import com.alex.yang.weather.core.network.Resource
import com.alex.yang.weather.core.network.safeApiCall
import com.alex.yang.weather.data.api.VisualCrossingWebServices
import com.alex.yang.weather.data.mapper.toDomain
import com.alex.yang.weather.domain.model.Timeline
import com.alex.yang.weather.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
class WeatherRepositoryImpl @Inject constructor(
    private val services: VisualCrossingWebServices
) : WeatherRepository {
    override suspend fun getWeather(
        location: String,
        startDay: String,
        endDay: String,
        apiKey: String
    ): Resource<Timeline> {
        return safeApiCall(
            request = {
                services.getWeather(
                    location = location,
                    startDay = startDay,
                    endDay = endDay,
                    key = apiKey
                )
            },
            response = { resp ->
                Resource.Success(resp.toDomain())
            }
        )
    }
}