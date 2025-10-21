package com.alex.yang.weather.data.mapper

import com.alex.yang.weather.data.model.DayDto
import com.alex.yang.weather.data.utils.iconToDrawable
import com.alex.yang.weather.data.utils.removeSeconds
import com.alex.yang.weather.domain.model.Day
import kotlin.math.roundToInt

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
fun DayDto.toDomain() =
    Day(
        datetime = datetime.orEmpty(),
        datetimeEpoch = datetimeEpoch ?: 0L,
        tempmax = tempmax?.roundToInt() ?: 0,
        tempmin = tempmin?.roundToInt() ?: 0,
        temp = temp?.roundToInt() ?: 0,
        feelslike = feelslike?.roundToInt() ?: 0,
        humidity = humidity ?: 0.0,
        dew = dew ?: 0.0,
        precipprob = precipprob ?: 0.0,
        windgust = windgust ?: 0.0,
        windspeed = windspeed ?: 0.0,
        pressure = pressure ?: 0.0,
        cloudcover = cloudcover ?: 0.0,
        visibility = visibility ?: 0.0,
        uvindex = uvindex ?: 0.0,
        sunrise = sunrise.removeSeconds(),
        sunset = sunset.removeSeconds(),
        conditions = conditions.orEmpty(),
        icon = icon.iconToDrawable(),
        hours = hours.map { it.toDomain() }
    )
