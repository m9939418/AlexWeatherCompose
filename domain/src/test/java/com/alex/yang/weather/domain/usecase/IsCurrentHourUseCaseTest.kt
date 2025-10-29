@file:Suppress("NonAsciiCharacters")

package com.alex.yang.weather.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test

/**
 * Created by AlexYang on 2025/10/18.
 *
 * 單元測試：針對 IsCurrentHourUseCase 進行測試
 *
 * 測試重點：
 * - 判斷輸入的 epochSec 是否落在「當前小時」區間
 *
 * 測試覆蓋情境
 *  EC    測試情境                                   預期行為
 * |-----|----------------------------------------|--------------------------------------|
 *  EC1  同一小時內（-5 分）→                        回傳 true
 *  EC2  前一小時（-1 小時 + 10 分）→                 回傳 false
 *  EC3  下一小時（+1 小時）→                         回傳 false
 *
 * 補強
 *  EC4  昨天同一小時（跨日但小時相同）→               回傳 false（避免僅比對時:分而誤判）
 *  EC5  邊界規則採用「左閉右開」[t, t+1h) →            整點屬於當前小時；下一小時整點不屬於
 */
class IsCurrentHourUseCaseTest {
    private val zone = DateTimeZone.forOffsetHours(8)
    private val useCase = IsCurrentHourUseCase()

    /** EC1：同一小時 → true */
    @Test
    fun `EC1 - 同一小時內（-5 分） 應為 true`() {
        // Given
        val baseNow = DateTime.now(zone).withMinuteOfHour(30).withSecondOfMinute(0).withMillisOfSecond(0)

        // When
        val withinSameHour = baseNow.minusMinutes(5).millis / 1000
        val result = useCase(withinSameHour, zone)

        // Then
        assertThat(result).isTrue()
    }

    /** EC2：前一小時 → false */
    @Test
    fun `EC2 - 前一小時（-1 小時 + 10 分） 應為 false`() {
        // Given
        val baseNow = DateTime.now(zone).withMinuteOfHour(30).withSecondOfMinute(0).withMillisOfSecond(0)

        // When
        val prevHour = baseNow.minusHours(1).withMinuteOfHour(10).millis / 1000
        val result = useCase(prevHour, zone)

        // Then
        assertThat(result).isFalse()
    }

    /** EC3：下一小時 → false */
    @Test
    fun `EC3 - 下一小時（+1 小時） 應為 false`() {
        // Given
        val baseNow = DateTime.now(zone).withMinuteOfHour(30).withSecondOfMinute(0).withMillisOfSecond(0)

        // When
        val nextHour = baseNow.plusHours(1).withMinuteOfHour(0).millis / 1000
        val result = useCase(nextHour, zone)

        // Then
        assertThat(result).isFalse()
    }

    /** EC4：昨天同一小時 → false */
    @Test
    fun `EC4 - 昨天同一小時 應為 false`() {
        // Given
        val baseNow = DateTime.now(zone).withMinuteOfHour(30).withSecondOfMinute(0).withMillisOfSecond(0)
        val yesterdaySameHour = baseNow.minusDays(1).withMinuteOfHour(5)

        // When
        val epoch = yesterdaySameHour.millis / 1000
        val result = useCase(epoch, zone)

        // Then
        assertThat(result).isFalse()
    }

    /** EC5：小時邊界（[t, t+1h)） */
    @Test
    fun `EC5 - 小時邊界 整點含括 下一小時整點排除`() {
        // Given
        val now = DateTime.now(zone).withMinuteOfHour(30).withSecondOfMinute(0).withMillisOfSecond(0)
        val hourStart = now.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
        val hourEnd = hourStart.plusHours(1)

        // When
        val startEpoch = hourStart.millis / 1000
        val endEpoch = hourEnd.millis / 1000
        val startResult = useCase(startEpoch, zone)
        val endResult = useCase(endEpoch, zone)

        // Then
        assertThat(startResult).isTrue()  // 整點屬於當前小時（左邊界含括）
        assertThat(endResult).isFalse()   // 下一小時整點不屬於（右邊界排除）
    }
}