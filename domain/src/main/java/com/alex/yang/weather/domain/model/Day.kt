@file:Suppress("SpellCheckingInspection")

package com.alex.yang.weather.domain.model

import androidx.compose.runtime.Immutable

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 * @property datetime      ISO 8601 的日期字串（當地時區），格式如：2025-10-16。
 * @property tempmax       當日最高氣溫（°C）。
 * @property tempmin       當日最低氣溫（°C）。
 * @property temp          當日平均氣溫（°C，日彙整的 mean 值）。
 * @property feelslike     當日平均體感溫度（°C，綜合風寒與熱指數）。
 * @property dew           露點溫度（°C）。
 * @property humidity      當日平均相對濕度（%）。
 * @property precipprob    當日最高降水機率（%，forecast 資料時可用）。
 * @property windgust      最大瞬間陣風風速（km/h），若無顯著高於平均風速則可能為空值。
 * @property windspeed     當日最大平均風速（km/h；日彙整為當日小時最大值）。
 * @property pressure      當日平均海平面氣壓（hPa / mbar）。
 * @property cloudcover    雲量覆蓋率平均能見度（%）。範圍 0–100，表示天空被雲遮蔽的比例。
 * @property visibility    當日（公里）。
 * @property uvindex       當日最大紫外線指數（0–10）。
 * @property sunrise       當日日出時間（ISO 8601，當地時區）。05:52:50
 * @property sunset        當日日落時間（ISO 8601，當地時區）。05:52:50
 * @property conditions    天氣文字描述（例如：晴朗、多雲、陣雨等）。
 * @property icon          天氣圖示代碼（例如：`clear-day`、`partly-cloudy-night`、`rain`），可用於對應圖示資源。
 * @property hours         逐小時天氣資料清單；只有在 API 請求 `include=hours` 時才會出現。
 */
@Immutable
data class Day(
    val datetime: String,
    val datetimeEpoch: Long,
    val tempmax: Int,
    val tempmin: Int,
    val temp: Int,
    val feelslike: Int,
    val humidity: Double,
    val dew: Double,
    val precipprob: Double,
    val windgust: Double,
    val windspeed: Double,
    val pressure: Double,
    val cloudcover: Double,
    val visibility: Double,
    val uvindex: Double,
    val sunrise: String,
    val sunset: String,
    val conditions: String,
    val icon: Int,
    val hours: List<Hour>
)
