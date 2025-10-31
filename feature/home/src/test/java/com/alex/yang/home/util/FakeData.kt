@file:Suppress("SpellCheckingInspection")

package com.alex.yang.home.util

import com.alex.yang.weather.domain.model.Counties
import com.alex.yang.weather.domain.model.County
import com.alex.yang.weather.domain.model.Day
import com.alex.yang.weather.domain.model.Hour
import com.alex.yang.weather.domain.model.Timeline
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * Created by AlexYang on 2025/10/31.
 *
 * 測試資料
 * 僅保留目前 HomeSharedViewModelTest / HomeViewModelTest 與資料層測試實際使用到的函式。
 */

// ----------------------------------------------------
// 日期格式
// ----------------------------------------------------
private val DATE_FMT: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
private val TIME_FMT: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm")

// ----------------------------------------------------
// 時間工具
// ----------------------------------------------------
fun todayString(): String =
    DateTime.now(DateTimeZone.getDefault()).withTimeAtStartOfDay().toString(DATE_FMT)

/** 以今日 00:00 為基準，回傳 epoch（秒） */
fun todayEpochAt(hour24: Int, minute: Int = 0, second: Int = 0): Long =
    DateTime.now(DateTimeZone.getDefault())
        .withTimeAtStartOfDay()
        .plusHours(hour24)
        .plusMinutes(minute)
        .plusSeconds(second)
        .millis / 1000

// ----------------------------------------------------
// County / Counties（RemoteConfig / Assets 測試會用到）
// ----------------------------------------------------
fun fakeCounty(zh: String = "台北市", en: String = "Taipei") = County(zh = zh, en = en)

fun fakeCounties(
    list: List<County> = listOf(
        fakeCounty("台北市", "Taipei"),
        fakeCounty("新北市", "New Taipei"),
        fakeCounty("桃園市", "Taoyuan"),
        fakeCounty("台中市", "Taichung"),
        fakeCounty("台南市", "Tainan"),
        fakeCounty("高雄市", "Kaohsiung"),
    ),
    version: Int = 1,
    updatedAt: String = "2025-10-18",
) = Counties(version = version, updatedAt = updatedAt, counties = list)

fun fakeCountiesJson(): String = """
{
  "version": 1,
  "updatedAt": "2025-10-18",
  "counties": [
    {"zh":"台北市","en":"Taipei"},
    {"zh":"新北市","en":"New Taipei"},
    {"zh":"桃園市","en":"Taoyuan"},
    {"zh":"台中市","en":"Taichung"},
    {"zh":"台南市","en":"Tainan"},
    {"zh":"高雄市","en":"Kaohsiung"},
    {"zh":"基隆市","en":"Keelung"},
    {"zh":"新竹市","en":"Hsinchu"},
    {"zh":"嘉義市","en":"Chiayi"},
    {"zh":"宜蘭縣","en":"Yilan"},
    {"zh":"新竹縣","en":"Hsinchu"},
    {"zh":"苗栗縣","en":"Miaoli"},
    {"zh":"彰化縣","en":"Changhua"},
    {"zh":"南投縣","en":"Nantou"},
    {"zh":"雲林縣","en":"Yunlin"},
    {"zh":"嘉義縣","en":"Chiayi"},
    {"zh":"屏東縣","en":"Pingtung"},
    {"zh":"澎湖縣","en":"Penghu"},
    {"zh":"台東縣","en":"Taitung"},
    {"zh":"花蓮縣","en":"Hualien"},
    {"zh":"金門縣","en":"Kinmen"},
    {"zh":"連江縣","en":"Lienchiang"}
  ]
}
""".trimIndent()

// ----------------------------------------------------
// Hour / Day Builders
// ----------------------------------------------------
fun fakeHour(
    datetime: String = "10:00",
    epoch: Long = 10_000L,
    temp: Int = 20,
): Hour = Hour(
    datetime = datetime,
    datetimeEpoch = epoch,
    temp = temp,
    feelslike = temp,
    humidity = 0.60,
    dew = 12.0,
    precipprob = 0,
    windgust = 0.0,
    windspeed = 2.0,
    pressure = 1010.0,
    cloudcover = 0,
    visibility = 10.0,
    uvindex = 5.0,
    sunrise = "06:00",
    sunset = "18:00",
    conditions = "Clear",
    icon = 0,
)

fun fakeDay(
    date: String = todayString(),
    epoch: Long = todayEpochAt(0),
    tmax: Int = 28,
    tmin: Int = 20,
    hours: List<Hour> = listOf(fakeHour(datetime = "00:00", epoch = epoch, temp = tmin)),
): Day = Day(
    datetime = date,
    datetimeEpoch = epoch,
    temp = ((tmax + tmin) / 2),
    feelslike = ((tmax + tmin) / 2),
    humidity = 0.60,
    dew = 12.0,
    windgust = 0.0,
    windspeed = 2.0,
    pressure = 1010.0,
    cloudcover = 0.2,
    visibility = 10.0,
    uvindex = 5.0,
    sunrise = "06:00",
    sunset = "18:00",
    conditions = "Clear",
    icon = 0,
    tempmax = tmax,
    tempmin = tmin,
    hours = hours,
    precipprob = 24.0,
)

// ----------------------------------------------------
// Timeline Builders（今日 / 自訂 / 空）
// ----------------------------------------------------
fun fakeTimelineForToday(): Timeline {
    val base = DateTime.now(DateTimeZone.getDefault())
        .withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
    val epoch = base.millis / 1000

    val h0 = fakeHour(datetime = "00:00", epoch = epoch - 3600, temp = 20)
    val h1 = fakeHour(datetime = "01:00", epoch = epoch, temp = 21)
    val h2 = fakeHour(datetime = "02:00", epoch = epoch + 3600, temp = 22)

    val today = fakeDay(
        date = DATE_FMT.print(base),
        epoch = epoch,
        tmax = 28,
        tmin = 20,
        hours = listOf(h0, h1, h2)
    )
    val tomorrow = fakeDay(
        date = DATE_FMT.print(base.plusDays(1)),
        epoch = epoch + 86_400L,
        tmax = 29,
        tmin = 21
    )

    return Timeline(
        queryCost = 1,
        address = "Taipei,TW",
        days = listOf(today, tomorrow),
        currentConditions = h1
    )
}

fun fakeTimeline(
    current: Hour = fakeHour(),
    days: List<Day> = listOf(fakeDay(hours = listOf(fakeHour("00:00", todayEpochAt(0), 20)))),
    queryCost: Int = 1,
    address: String = "Taipei,TW",
): Timeline = Timeline(
    queryCost = queryCost,
    address = address,
    days = days,
    currentConditions = current
)

fun fakeEmptyTimeline(): Timeline =
    Timeline(
        queryCost = 0,
        address = "Unknown",
        days = emptyList(),
        currentConditions = fakeHour(datetime = "00:00", epoch = todayEpochAt(0), temp = 20)
    )