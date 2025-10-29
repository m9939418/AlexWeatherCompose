@file:Suppress("NonAsciiCharacters")

package com.alex.yang.weather.domain.usecase

import com.alex.yang.weather.domain.model.Day
import com.alex.yang.weather.domain.utils.FakeWeatherFactory
import com.google.common.truth.Truth.assertThat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test

/**
 * Created by AlexYang on 2025/10/18.
 *
 * 單元測試：針對 FindTodayDayUseCase 進行測試
 *
 * 測試重點：
 * - 從 days 清單中找出「今天（依指定時區）」對應的 Day
 *
 * 測試覆蓋情境
 *  EC    測試情境                                   預期行為
 * |-----|----------------------------------------|--------------------------------------|
 *  EC1  days 含有今天                              → 回傳今天的 Day
 *  EC2  days 不含今天                              → 回傳 null
 *  EC3  days 為空清單                              → 回傳 null
 */
class FindTodayDayUseCaseTest {
    private val zone: DateTimeZone = DateTimeZone.forOffsetHours(8)
    private val useCase = FindTodayDayUseCase()

    /** EC1：days 含有今天 → 回傳今天的 Day */
    @Test
    fun `EC1 - days 含有今天 應回傳今天`() {
        // Given
        val baseNow = DateTime.now(zone).withTime(10, 0, 0, 0)
        val todayEpochSec = baseNow.millis / 1000
        val yesterday = baseNow.minusDays(1)

        val dYesterday = FakeWeatherFactory.createDay(
            datetime = yesterday.toString("yyyy-MM-dd"),
            datetimeEpoch = yesterday.millis / 1000
        )
        val dToday = FakeWeatherFactory.createDay(
            datetime = baseNow.toString("yyyy-MM-dd"),
            datetimeEpoch = todayEpochSec
        )

        // When
        val result = useCase(days = listOf(dYesterday, dToday), zone = zone)

        // Then
        assertThat(result).isNotNull()
        assertThat(result?.datetimeEpoch).isEqualTo(todayEpochSec)
    }

    /** EC2：days 不含今天 → 回傳 null */
    @Test
    fun `EC2 - days 不含今天 應回傳 null`() {
        // Given
        val baseNow = DateTime.now(zone).withTime(10, 0, 0, 0)
        val twoDaysAgo = baseNow.minusDays(2)
        val yesterday = baseNow.minusDays(1)

        val dTwoDaysAgo = FakeWeatherFactory.createDay(
            datetime = twoDaysAgo.toString("yyyy-MM-dd"),
            datetimeEpoch = twoDaysAgo.millis / 1000
        )
        val dYesterday = FakeWeatherFactory.createDay(
            datetime = yesterday.toString("yyyy-MM-dd"),
            datetimeEpoch = yesterday.millis / 1000
        )

        // When
        val result = useCase(days = listOf(dTwoDaysAgo, dYesterday), zone = zone)

        // Then
        assertThat(result).isNull()
    }

    /** EC3：days 為空清單 → 回傳 null */
    @Test
    fun `EC3 - days 為空 應回傳 null`() {
        // Given
        val empty = emptyList<Day>()

        // When
        val result = useCase(empty, zone)

        // Then
        assertThat(result).isNull()
    }
}
 