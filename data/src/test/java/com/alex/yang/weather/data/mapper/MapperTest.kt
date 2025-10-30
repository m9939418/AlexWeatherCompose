@file:Suppress("NonAsciiCharacters")

package com.alex.yang.weather.data.mapper

import com.alex.yang.weather.data.model.DayDto
import com.alex.yang.weather.data.model.HourDto
import com.alex.yang.weather.data.model.TimelineDto
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

/**
 * Created by AlexYang on 2025/10/29.
 *
 * 單元測試：針對 Mapper (Hour/Day/Timeline) 進行測試
 *
 * 測試重點：
 * - HourDto → Hour：數值四捨五入、時間裁秒、icon 對應與 conditions 字串處理
 * - DayDto  → Day ：巢狀 hours 正確轉換與欄位四捨五入
 * - TimelineDto → Timeline：currentConditions 缺省時使用預設
 *
 * 測試覆蓋情境
 *  EC    測試情境                                                     預期行為
 * |-----|----------------------------------------------------------|--------------------------------------|
 *  EC1  HourDto 轉換（四捨五入、裁秒、icon 對應、conditions 非空）         各欄位正確，conditions 非空字串
 *  EC2  DayDto 轉換（包含巢狀 hours）                                   日期、最高溫四捨五入，hours 轉換正確
 *  EC3  TimelineDto 轉換（currentConditions=null 時使用預設 Hour.DEFAULT） address 不變，currentConditions 不為 null
 */
class MapperTest {

    /**
     * EC1：HourDto 轉換（四捨五入、裁秒、icon 對應、conditions 非空） → 應正確
     */
    @Test
    fun `EC1 - HourDto 轉換（四捨五入、裁秒、icon 對應、conditions 非空） 應正確`() {
        // Given
        val data = HourDto(
            datetime = "04:00:00",
            temp = 31.6,
            feelslike = 33.4,
            cloudcover = 12.0,
            sunrise = "05:10:00",
            sunset = "17:30:00",
            icon = "rain",
            conditions = "晴時"
        )

        // When
        val result = data.toDomain()

        // Then
        assertThat(result.datetime).isEqualTo("04:00")
        assertThat(result.temp).isEqualTo(32)
        assertThat(result.sunrise).isEqualTo("05:10")
        assertThat(result.sunset).isEqualTo("17:30")
        assertThat(result.icon).isNotEqualTo(0)
        assertThat(result.conditions).isNotEmpty()
    }

    /**
     * EC2：DayDto 轉換（包含巢狀 hours） → 應正確
     */
    @Test
    fun `EC2 - DayDto 轉換（包含巢狀 hours） 應正確`() {
        // Given
        val data = DayDto(
            datetime = "2025-10-16",
            tempmax = 32.2,
            tempmin = 25.0,
            hours = listOf(HourDto(datetime = "14:00:00"))
        )

        // When
        val result = data.toDomain()

        // Then
        assertThat(result.datetime).isEqualTo("2025-10-16")
        assertThat(result.tempmax).isEqualTo(32)
        assertThat(result.hours).hasSize(1)
        assertThat(result.hours.first().datetime).isEqualTo("14:00")
    }

    /**
     * EC3：TimelineDto 轉換（currentConditions = null） → 應使用預設
     */
    @Test
    fun `EC3 - TimelineDto 轉換（currentConditions 為 null 時使用預設） 應正確`() {
        // Given
        val data = TimelineDto(
            address = "Taipei,TW",
            days = emptyList(),
            currentConditions = null
        )

        // When
        val result = data.toDomain()

        // Then
        assertThat(result.address).isEqualTo("Taipei,TW")
        assertThat(result.currentConditions).isNotNull()
    }
}