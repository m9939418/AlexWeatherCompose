package com.alex.yang.weather.data.utils

import androidx.annotation.DrawableRes
import com.alex.yang.weather.data.R

/**
 * Created by AlexYang on 2025/10/19.
 *
 *
 */

fun String?.removeSeconds(): String {
    return this?.substringBeforeLast(":").orEmpty()
}

@DrawableRes
fun String?.iconToDrawable(): Int {
    return this?.let { icon ->
        when (icon) {
            "clear-day" -> R.drawable.clear_day
            "clear-night" -> R.drawable.cloudy
            "cloudy" -> R.drawable.cloudy
            "fog" -> R.drawable.fog
            "hail" -> R.drawable.hail
            "partly-cloudy-day" -> R.drawable.partly_cloudy_day
            "partly-cloudy-night" -> R.drawable.partly_cloudy_night
            "rain-snow-showers-day" -> R.drawable.rain_snow_showers_day
            "rain-snow-showers-night" -> R.drawable.rain_snow_showers_night
            "rain-snow" -> R.drawable.rain_snow
            "rain" -> R.drawable.rain
            "showers-day" -> R.drawable.showers_day
            "showers-night" -> R.drawable.showers_night
            "sleet" -> R.drawable.sleet
            "snow-showers-day" -> R.drawable.snow_showers_day
            "snow-showers-night" -> R.drawable.snow_showers_night
            "snow" -> R.drawable.snow
            "thunder-rain" -> R.drawable.thunder_rain
            "thunder-showers-day" -> R.drawable.thunder_showers_day
            "thunder-showers-night" -> R.drawable.thunder_showers_night
            "thunder" -> R.drawable.thunder
            "wind" -> R.drawable.wind
            else -> R.drawable.clear_day
        }
    } ?: return R.drawable.clear_day
}