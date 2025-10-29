@file:Suppress("SpellCheckingInspection")

package com.alex.yang.weather.domain.utils

import com.alex.yang.weather.domain.model.Day
import com.alex.yang.weather.domain.model.Hour
import com.alex.yang.weather.domain.model.Timeline

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
object FakeWeatherFactory {
    fun createHour(
        datetime: String = "10:00",
        datetimeEpoch: Long,
        temp: Int = 26,
        feelslike: Int = 26,
        humidity: Double = 60.0,
        dew: Double = 18.0,
        precipprob: Int = 20,
        windgust: Double = 12.0,
        windspeed: Double = 6.0,
        pressure: Double = 1015.0,
        cloudcover: Int = 30,
        visibility: Double = 10.0,
        uvindex: Double = 5.0,
        sunrise: String = "",
        sunset: String = "",
        conditions: String = "Cloudy",
        icon: Int = 0,
    ) = Hour(
        datetime = datetime,
        datetimeEpoch = datetimeEpoch,
        temp = temp,
        feelslike = feelslike,
        humidity = humidity,
        dew = dew,
        precipprob = precipprob,
        windgust = windgust,
        windspeed = windspeed,
        pressure = pressure,
        cloudcover = cloudcover,
        visibility = visibility,
        uvindex = uvindex,
        sunrise = sunrise,
        sunset = sunset,
        conditions = conditions,
        icon = icon
    )

    fun createDay(
        datetime: String,
        datetimeEpoch: Long,
        tempmax: Int = 30,
        tempmin: Int = 22,
        temp: Int = 26,
        feelslike: Int = 26,
        humidity: Double = 60.0,
        dew: Double = 18.0,
        precipprob: Double = 20.0,
        windgust: Double = 12.0,
        windspeed: Double = 6.0,
        pressure: Double = 1015.0,
        cloudcover: Double = 30.0,
        visibility: Double = 10.0,
        uvindex: Double = 5.0,
        sunrise: String = "06:00",
        sunset: String = "17:30",
        conditions: String = "Cloudy",
        icon: Int = 0,
        hours: List<Hour> = emptyList()
    ) = Day(
        datetime = datetime,
        datetimeEpoch = datetimeEpoch,
        tempmax = tempmax,
        tempmin = tempmin,
        temp = temp,
        feelslike = feelslike,
        humidity = humidity,
        dew = dew,
        precipprob = precipprob,
        windgust = windgust,
        windspeed = windspeed,
        pressure = pressure,
        cloudcover = cloudcover,
        visibility = visibility,
        uvindex = uvindex,
        sunrise = sunrise,
        sunset = sunset,
        conditions = conditions,
        icon = icon,
        hours = hours
    )

    fun createTimeline(
        address: String,
        days: List<Day>,
        current: Hour = Hour.DEFAULT,
        queryCost: Int = 1
    ) = Timeline(
        queryCost = queryCost,
        address = address,
        days = days,
        currentConditions = current
    )
}