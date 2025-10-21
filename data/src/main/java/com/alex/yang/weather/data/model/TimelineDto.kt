package com.alex.yang.weather.data.model

import kotlinx.serialization.Serializable

/**
 * Created by AlexYang on 2025/10/15.
 *
 * @property queryCost          Server 查詢成本（API 回傳的運算單位）。
 * @property address            查詢地點字串（例如 "Taipei,TW"）。
 * @property days               每日天氣清單（含每小時資料）。
 * @property currentConditions  目前即時天氣條件。
 */
@Serializable
data class TimelineDto(
    val queryCost: Int? = null,
    val address: String? = null,
    val days: List<DayDto> = emptyList(),
    val currentConditions: HourDto? = null
)