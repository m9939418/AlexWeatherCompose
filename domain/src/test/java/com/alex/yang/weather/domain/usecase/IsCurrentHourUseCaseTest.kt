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
 * - 驗證傳入的 epoch 秒數是否屬於「當前小時」
 * - 確認前一小時、下一小時等情境皆能正確判斷
 *
 * 測試覆蓋情境
 *  EC    測試情境                             預期行為
 * |-----|----------------------------------|----------------------------------------|
 *  EC1  輸入目前小時的 epoch                 → 回傳 true
 *  EC2  輸入前一小時的 epoch                 → 回傳 false
 *  EC3  輸入下一小時的 epoch                 → 回傳 false
 */
class IsCurrentHourUseCaseTest {
    private val useCase = IsCurrentHourUseCase()
    private val zone = DateTimeZone.getDefault()

    /**
     * EC1：輸入目前小時 → 回傳 true
     */
    @Test
    fun `EC1 - 輸入目前小時 應回傳 true`() {
        // Given:
        val now = DateTime.now(zone)

        // When:
        val result = useCase(now.millis / 1000, zone)

        // Then:
        assertThat(result).isTrue()
    }

    /**
     * EC2：輸入前一小時 → 回傳 false
     */
    @Test
    fun `EC2 - 輸入前一小時 應回傳 false`() {
        // Given:
        val prevHour = DateTime.now(zone).minusHours(1)

        // When:
        val result = useCase(prevHour.millis / 1000, zone)

        // Then:
        assertThat(result).isFalse()
    }

    /**
     * EC3：輸入下一小時 → 回傳 false
     */
    @Test
    fun `EC3 - 輸入下一小時 應回傳 false`() {
        // Given:
        val nextHour = DateTime.now(zone).plusHours(1)

        // When:
        val result = useCase(nextHour.millis / 1000, zone)

        // Then:
        assertThat(result).isFalse()
    }
}