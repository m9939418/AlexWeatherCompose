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
 * - 驗證傳入的 epoch 秒數是否正確判斷為「今天」
 * - 確認跨日、跨時區情境下的邏輯正確性
 *
 * 測試覆蓋情境
 *  EC    測試情境                           預期行為
 * |-----|--------------------------------|----------------------------------------------|
 *  EC1  輸入今天任意時間 epoch             → 回傳 true
 *  EC2  輸入昨天任意時間 epoch             → 回傳 false
 *  EC3  輸入明天任意時間 epoch             → 回傳 false
 */
class IsTodayUseCaseTest {
    private val useCase = IsTodayUseCase()
    private val zone = DateTimeZone.getDefault()

    /**
     * EC1：輸入今天時間 → 回傳 true
     */
    @Test
    fun `EC1 - 輸入今天任意時間 應回傳 true`() {
        // Given:
        val today = DateTime.now(zone).withTimeAtStartOfDay().plusHours(10)

        // When:
        val result = useCase(today.millis / 1000, zone)

        // Then:
        assertThat(result).isTrue()
    }

    /**
     * EC2：輸入昨天時間 → 回傳 false
     */
    @Test
    fun `EC2 - 輸入昨天時間 應回傳 false`() {
        // Given:
        val yesterday = DateTime.now(zone).minusDays(1).withTimeAtStartOfDay()

        // When:
        val result = useCase(yesterday.millis / 1000, zone)

        // Then:
        assertThat(result).isFalse()
    }

    /**
     * EC3：輸入明天時間 → 回傳 false
     */
    @Test
    fun `EC3 - 輸入明天時間 應回傳 false`() {
        // Given:
        val tomorrow = DateTime.now(zone).plusDays(1).withTimeAtStartOfDay()

        // When:
        val result = useCase(tomorrow.millis / 1000, zone)

        // Then:
        assertThat(result).isFalse()
    }
}