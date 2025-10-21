@file:Suppress("NonAsciiCharacters")

package com.alex.yang.weather.domain.usecase

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
 * - 驗證在 days 清單中能正確找到「今天」的 Day
 * - 確認沒有符合條件時回傳 null
 *
 * 測試覆蓋情境
 *  EC    測試情境                                   預期行為
 * |-----|----------------------------------------|--------------------------------------|
 *  EC1  days 清單包含今天的日期 →                    回傳對應 Day
 *  EC2  days 清單沒有今天的日期 →                    回傳 null
 */
class FindTodayDayUseCaseTest {
    private val useCase = FindTodayDayUseCase()
    private val zone = DateTimeZone.getDefault()
    val today = FakeWeatherFactory.createDay()
    val yesterday = FakeWeatherFactory.createDay(datetime = "2025-10-16", epoch = 1729056000L)

    /**
     * EC1：days 含有今天 → 回傳今天的 Day
     */
    @Test
    fun `EC1 - days 含今天日期 應回傳對應 Day`() {
        // Given:
        val todayEpoch = DateTime.now(zone).withTimeAtStartOfDay().millis / 1000
        val days = listOf(today, yesterday)

        // When:
        val result = useCase(days, zone)

        // Then:
        assertThat(result?.datetimeEpoch).isEqualTo(todayEpoch)
    }

    /**
     * EC2：days 不含今天 → 回傳 null
     */
    @Test
    fun `EC2 - days 不含今天日期 應回傳 null`() {
        // Given:
        val days = listOf(yesterday)

        // When:
        val result = useCase(days, zone)

        // Then:
        assertThat(result).isNull()
    }
}
 