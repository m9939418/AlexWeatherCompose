package com.alex.yang.weather.domain.model

import androidx.compose.runtime.Immutable

/**
 * Created by AlexYang on 2025/10/16.
 *
 * @property queryCost          Server 查詢成本（API 回傳的運算單位）。
 * @property address            查詢地點字串（例如 "Taipei,TW"）。
 * @property days               每日天氣清單（含每小時資料）。
 * @property currentConditions  目前即時天氣條件。
 */
@Immutable
data class Timeline(
    val queryCost: Int,
    val address: String,
    val days: List<Day>,
    val currentConditions: Hour
)