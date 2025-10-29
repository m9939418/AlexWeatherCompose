package com.alex.yang.weather.domain.usecase

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/18.
 *
 * 判斷某 epoch 時間是否為當前小時
 */
class IsCurrentHourUseCase @Inject constructor() {
    operator fun invoke(
        epochSec: Long,
        zone: DateTimeZone = DateTimeZone.getDefault()
    ): Boolean {
        val now = DateTime.now(zone)
        val startOfThisHour = now.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
        val endExclusive = startOfThisHour.plusHours(1)

        val target = DateTime(epochSec * 1000L, zone)
        // [start, end)
        return !target.isBefore(startOfThisHour) && target.isBefore(endExclusive)
    }
}
