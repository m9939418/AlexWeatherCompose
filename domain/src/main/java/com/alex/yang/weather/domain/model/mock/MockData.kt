package com.alex.yang.weather.domain.model.mock

import com.alex.yang.weather.domain.model.Day
import com.alex.yang.weather.domain.model.Hour
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Created by AlexYang on 2025/10/20.
 *
 * 產生 7 天 × 24 小時的 Preview 假資料（當地時區）
 */
fun fakeWeeklyDaysForPreview(
    zone: DateTimeZone = DateTimeZone.getDefault()
): List<Day> {
    val today0 = DateTime.now(zone).withTimeAtStartOfDay()

    return (0 until 7).map { d ->
        val dayStart = today0.plusDays(d)
        val hours: List<Hour> = (0 until 24).map { h ->
            val t = dayStart.plusHours(h)
            Hour(
                datetime = t.toString("HH:mm:ss", Locale.TAIWAN),
                datetimeEpoch = t.millis / 1000,
                temp = (22 + (h / 6)).coerceAtMost(30),
                feelslike = (22 + (h / 6) + 1).coerceAtMost(31),
                humidity = 70.0 + (h % 5) * 3,   // 70~82
                dew = 18.0 + (h % 4),           // 18~21
                precipprob = (h % 6) * 10,      // 0,10,...,50
                windgust = 15.0 + (h % 7) * 2,  // 15~27
                windspeed = 8.0 + (h % 6) * 1,  // 8~13
                pressure = 1010.0 + (h % 3),    // 1010~1012
                cloudcover = ((40 + h) % 100),  // 0~99
                visibility = 10.0,
                uvindex = (if (h in 10..14) 4 else 0).toDouble(),
                sunrise = dayStart.plusHours(6).toString("HH:mm:ss", Locale.TAIWAN),
                sunset = dayStart.plusHours(18).toString("HH:mm:ss", Locale.TAIWAN),
                conditions = if (h in 13..16) "陣雨" else "多雲",
                icon = android.R.drawable.ic_menu_compass
            )
        }

        // 用小時計算當日統計
        val temps = hours.map { it.temp }
        val tempAvg = temps.average().roundToInt()

        Day(
            datetime = dayStart.toString("yyyy-MM-dd", Locale.TAIWAN),
            datetimeEpoch = dayStart.millis / 1000,
            tempmax = temps.maxOrNull() ?: tempAvg,
            tempmin = temps.minOrNull() ?: tempAvg,
            temp = tempAvg,
            feelslike = (tempAvg + 1),
            humidity = hours.map { it.humidity }.average(),
            dew = hours.map { it.dew }.average(),
            precipprob = hours.maxOf { it.precipprob }.toDouble(),
            windgust = hours.maxOf { it.windgust },
            windspeed = hours.maxOf { it.windspeed },
            pressure = hours.map { it.pressure }.average(),
            cloudcover = hours.map { it.cloudcover }.average(),
            visibility = 10.0,
            uvindex = hours.maxOf { it.uvindex },
            sunrise = dayStart.plusHours(6).toString("HH:mm:ss", Locale.TAIWAN),
            sunset = dayStart.plusHours(18).toString("HH:mm:ss", Locale.TAIWAN),
            conditions = "多雲",
            icon = android.R.drawable.ic_menu_compass,
            hours = hours
        )
    }
}