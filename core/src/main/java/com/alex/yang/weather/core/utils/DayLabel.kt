package com.alex.yang.weather.core.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.util.Locale

/**
 * Created by AlexYang on 2025/10/17.
 *
 *
 */
object DayLabel {
    private val defaultZone = DateTimeZone.getDefault()
    private val formatter = DateTimeFormat.forPattern("MM/dd").withLocale(Locale.TAIWAN)
    private val fmtWeeklyDetail = DateTimeFormat.forPattern("MM/dd\nE").withLocale(Locale.TAIWAN)
    private val weekNames = arrayOf("一", "二", "三", "四", "五", "六", "日")
    // Joda 的 dayOfWeek: 1=Monday,...,7=Sunday
    private fun weekName(dow: Int) = "週 " + weekNames[(if (dow == 7) 6 else dow - 1)]

    /**
     * 回傳 Pair(上行文字, 下行文字)
     * 例： ("今天", "10/17") 或 ("週六", "10/18")
     */
    fun label(
        epochSec: Long,
    ): Pair<String, String> {
        val day = DateTime(epochSec * 1000L, defaultZone).withTimeAtStartOfDay()
        val today = DateTime.now(defaultZone).withTimeAtStartOfDay()

        val top = when {
            day == today -> "今 天"
            day == today.plusDays(1) -> "明 天"
            else -> weekName(day.dayOfWeek)
        }
        val bottom = formatter.print(day) // "MM/dd"

        return top to bottom
    }

    fun fmtWeeklyDetailTabLabel(epochSec: Long): String =
        DateTime(epochSec * 1000L, defaultZone).toString(fmtWeeklyDetail)

}