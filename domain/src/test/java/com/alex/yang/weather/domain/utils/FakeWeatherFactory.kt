package com.alex.yang.weather.domain.utils

import com.alex.yang.weather.domain.model.Day
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
object FakeWeatherFactory {
    fun createDay(
        datetime: String = DateTime.now().toString("yyyy-MM-dd"),
        epoch: Long = DateTime.now(DateTimeZone.getDefault())
            .withTimeAtStartOfDay()
            .millis / 1000
    ): Day {
        return Day(
            datetime = datetime,
            datetimeEpoch = epoch,
            tempmax = 32.0,
            tempmin = 24.0,
            temp = 28.0,
            feelslike = 29.0,
            humidity = 65.0,
            dew = 22.0,
            precipprob = 10.0,
            windgust = 15.0,
            windspeed = 8.0,
            pressure = 1013.0,
            cloudcover = 30.0,
            visibility = 10.0,
            uvindex = 6.0,
            sunrise = "${datetime}T06:00:00",
            sunset = "${datetime}T17:45:00",
            conditions = "晴時多雲",
            icon = "partly-cloudy-day",
            hours = emptyList()
        )
    }
}