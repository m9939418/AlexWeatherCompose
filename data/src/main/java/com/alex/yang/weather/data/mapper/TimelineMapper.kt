package com.alex.yang.weather.data.mapper

import com.alex.yang.weather.data.model.TimelineDto
import com.alex.yang.weather.domain.model.Hour
import com.alex.yang.weather.domain.model.Timeline

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */

fun TimelineDto.toDomain() =
    Timeline(
        queryCost = queryCost ?: 0,
        address = address.orEmpty(),
        days = days.map { it.toDomain() },
        currentConditions = currentConditions?.toDomain()
            ?: Hour.DEFAULT
    )