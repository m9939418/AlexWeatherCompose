@file:Suppress("NonAsciiCharacters")

package com.alex.yang.weather.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test

/**
 * Created by AlexYang on 2025/10/18.
 *
 * 單元測試：針對 IsTodayUseCase 進行測試
 *
 * 測試重點：
 * - 判斷輸入的 epochSec 是否為「今天（依指定時區）」
 *
 * 測試覆蓋情境
 *  EC    測試情境                                   預期行為
 * |-----|----------------------------------------|--------------------------------------|
 *  EC1  同一天（同一時區內前後小時）→                 回傳 true
 *  EC2  昨天（不同日期）→                            回傳 false
 */
class IsTodayUseCaseTest {
    private val zone = DateTimeZone.forOffsetHours(8)
    private val useCase = IsTodayUseCase()

    /**
     * EC1：同一天 → true
     */
    @Test
    fun `EC1 - 同一天（同一時區內前後小時） 應為 true`() {
        // Given
        val baseNow = DateTime.now(zone).withTime(15, 0, 0, 0)
        val epochSecSameDay = baseNow.minusHours(1).millis / 1000

        // When
        val result = useCase(epochSecSameDay, zone)

        // Then
        assertThat(result).isTrue()
    }

    /**
     * EC2：昨天 → false
     */
    @Test
    fun `EC2 - 昨天（不同日期） 應為 false`() {
        // Given
        val baseNow = DateTime.now(zone).withTime(15, 0, 0, 0)
        val epochSecYesterday = baseNow.minusDays(1).withTime(12, 0, 0, 0).millis / 1000

        // When
        val result = useCase(epochSecYesterday, zone)

        // Then
        assertThat(result).isFalse()
    }
}