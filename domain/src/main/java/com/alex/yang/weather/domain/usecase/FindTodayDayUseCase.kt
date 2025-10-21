package com.alex.yang.weather.domain.usecase

import com.alex.yang.weather.domain.model.Day
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/18.
 *
 * 尋找今天的 Day 資料（以 epoch 與時區計算，避免 DST/跨區問題）
 */
class FindTodayDayUseCase @Inject constructor() {
    operator fun invoke(days: List<Day>, zone: DateTimeZone = DateTimeZone.getDefault()): Day? {
        val today = DateTime.now(zone).withTimeAtStartOfDay()
        return days.firstOrNull { d ->
            DateTime(d.datetimeEpoch * 1000L, zone).withTimeAtStartOfDay() == today
        }
    }
}