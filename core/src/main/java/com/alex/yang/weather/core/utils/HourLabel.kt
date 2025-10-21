package com.alex.yang.weather.core.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.util.Locale

/**
 * Created by AlexYang on 2025/10/17.
 *
 * 日期時間格式轉換
 */
object HourLabel {
    private val formatter = DateTimeFormat.forPattern("ah時").withLocale(Locale.TAIWAN) // 下午1時
    private val weekFormatter = DateTimeFormat.forPattern("E").withLocale(Locale.TAIWAN) // 週日
    private val dateFormatter = DateTimeFormat.forPattern("MM/dd").withLocale(Locale.TAIWAN) // 10/19

    /**
     * 將 epoch 秒轉成「現在 / 下午3時」格式
     */
    fun label(epochSec: Long): String {
        val item = DateTime(epochSec * 1000L, DateTimeZone.getDefault())

        val nowHour = DateTime.now(DateTimeZone.getDefault()).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
        val itemHour = item.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)

        return if (itemHour == nowHour) "現在" else formatter.print(item)
    }

    /**
     * 將現在時間轉成「週日 10/19」格式
     */
    val todayLabel
        get(): String = DateTime.now(DateTimeZone.getDefault()).let { now ->
            "${dateFormatter.print(now)} ${weekFormatter.print(now)}"
        }
}