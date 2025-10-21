@file:Suppress("SpellCheckingInspection")

package com.alex.yang.weather.domain.model

import kotlinx.serialization.Serializable
import javax.annotation.concurrent.Immutable

/**
 * Created by AlexYang on 2025/10/16.
 *
 * @property datetime      ISO 8601 格式的日期時間字串，表示當地時間（例：2025-10-16T14:00:00）。
 * @property temp          當時溫度（°C），為氣溫原始值。
 * @property feelslike     體感溫度（°C），考量風寒或熱指數的感受溫度。
 * @property humidity      相對濕度（%）。
 * @property dew           露點溫度（°C）。
 * @property precipprob    降水機率（%），表示該時段可能出現降水的機率。
 * @property windgust      最大瞬間陣風風速（km/h），若無顯著高於平均風速則可能為空值。
 * @property windspeed     平均風速（km/h）。
 * @property pressure      海平面氣壓（hPa / mbar）。
 * @property cloudcover    雲量覆蓋率（%）。範圍 0–100，表示天空被雲遮蔽的比例。
 * @property visibility    能見度（公里）。
 * @property uvindex       紫外線指數，範圍 0–10，10 代表曝曬風險最高。
 * @property sunrise       當日日出時間（ISO 8601，當地時區）。
 * @property sunset        當日日落時間（ISO 8601，當地時區）。
 * @property conditions    天氣文字描述（例：「多雲」、「陣雨」、「晴朗」等）。
 * @property icon          天氣圖示代碼（例：`clear-day`、`partly-cloudy-night`、`rain`），可用於對應顯示圖示。
 */
@Immutable
@Serializable
data class Hour(
    val datetime: String,
    val datetimeEpoch: Long,
    val temp: Int,
    val feelslike: Int,
    val humidity: Double,
    val dew: Double,
    val precipprob: Int,
    val windgust: Double,
    val windspeed: Double,
    val pressure: Double,
    val cloudcover: Int,
    val visibility: Double,
    val uvindex: Double,
    val sunrise: String,
    val sunset: String,
    val conditions: String,
    val icon: Int
) {
    companion object {
        val DEFAULT = Hour(
            datetime = "",
            datetimeEpoch = 0L,
            temp = 0,
            feelslike = 0,
            humidity = Double.NaN,
            dew = Double.NaN,
            precipprob = 0,
            windgust = Double.NaN,
            windspeed = Double.NaN,
            pressure = Double.NaN,
            cloudcover = 0,
            visibility = Double.NaN,
            uvindex = Double.NaN,
            sunrise = "",
            sunset = "",
            conditions = "",
            icon = 0
        )
    }
}
