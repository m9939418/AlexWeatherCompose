@file:Suppress("SpellCheckingInspection")

package com.alex.yang.weather.data.model

import kotlinx.serialization.Serializable

/**
 * Created by AlexYang on 2025/10/15.
 *
 * 單筆逐小時天氣資料模型 (HourDto)
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
 * @property conditions    天氣文字描述（例：「多雲」、「陣雨」、「晴朗」等）。
 * @property icon          天氣圖示代碼（例：`clear-day`、`partly-cloudy-night`、`rain`），可用於對應顯示圖示。
 */
@Serializable
data class HourDto(
    val datetime: String? = null,
    val datetimeEpoch: Long? = 0L,
    val temp: Double? = null,
    val feelslike: Double? = null,
    val humidity: Double? = null,
    val dew: Double? = null,
    val precipprob: Double? = null,
    val windgust: Double? = null,
    val windspeed: Double? = null,
    val pressure: Double? = null,
    val cloudcover: Double? = null,
    val visibility: Double? = null,
    val uvindex: Double? = null,
    val sunrise: String? = null,
    val sunset: String? = null,
    val conditions: String? = null,
    val icon: String? = null
)
