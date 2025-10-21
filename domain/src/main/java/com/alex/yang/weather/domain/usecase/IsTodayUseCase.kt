package com.alex.yang.weather.domain.usecase

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/18.
 *
 * 判斷某 epoch（秒）是否為「今天」。
 * 以時區計算，避免 DST / 跨區造成誤判。
 */
class IsTodayUseCase @Inject constructor() {
    operator fun invoke(
        epochSec: Long,
        zone: DateTimeZone = DateTimeZone.getDefault()
    ): Boolean {
        val todayStart = DateTime.now(zone).withTimeAtStartOfDay()
        val targetDayStart = DateTime(epochSec * 1000L, zone).withTimeAtStartOfDay()
        return targetDayStart == todayStart
    }
}