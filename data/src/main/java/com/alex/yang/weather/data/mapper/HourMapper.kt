package com.alex.yang.weather.data.mapper

import com.alex.yang.weather.data.model.HourDto
import com.alex.yang.weather.data.utils.iconToDrawable
import com.alex.yang.weather.data.utils.removeSeconds
import com.alex.yang.weather.domain.model.Hour
import kotlin.math.roundToInt

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
fun HourDto.toDomain() =
    Hour(
        datetime = datetime.removeSeconds(),
        datetimeEpoch = datetimeEpoch ?: 0L,
        temp = temp?.roundToInt() ?: 0,
        feelslike = feelslike?.roundToInt() ?: 0,
        humidity = humidity ?: 0.0,
        dew = dew ?: 0.0,
        precipprob = precipprob?.roundToInt() ?: 0,
        windgust = windgust ?: 0.0,
        windspeed = windspeed ?: 0.0,
        pressure = pressure ?: 0.0,
        cloudcover = cloudcover?.roundToInt() ?: 0,
        visibility = visibility ?: 0.0,
        uvindex = uvindex ?: 0.0,
        conditions = conditions.orEmpty(),
        sunrise = sunrise.removeSeconds(),
        sunset = sunset.removeSeconds(),
        icon = icon.iconToDrawable()
    )