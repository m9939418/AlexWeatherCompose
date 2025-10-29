@file:Suppress("NonAsciiCharacters")

package com.alex.yang.weather.domain.usecase

import com.alex.yang.weather.core.network.Resource
import com.alex.yang.weather.domain.model.Hour
import com.alex.yang.weather.domain.repository.WeatherRepository
import com.alex.yang.weather.domain.utils.FakeWeatherFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Created by AlexYang on 2025/10/29.
 *
 * 單元測試：針對 GetWeatherUseCase 進行測試
 *
 * 測試重點：
 * - 驗證 UseCase 對 Repository 的轉發行為（Success / Error）
 * - 驗證互動參數是否正確（coVerify）
 *
 * 測試覆蓋情境
 *  EC    測試情境                                   預期行為
 * |-----|----------------------------------------|--------------------------------------|
 *  EC1  Repository 回傳 Success →                  UseCase 應轉交 Success 並正確帶參數
 *  EC2  Repository 回傳 Error →                    UseCase 應轉交 Error（訊息一致）
 */
class GetWeatherUseCaseTest {
    private val repo: WeatherRepository = mockk()
    private val useCase = GetWeatherUseCase(repo)

    /** EC1：Success → 轉交成功並驗證參數 */
    @Test
    fun `EC1 - Repository 回傳 Success 應轉交 Success 並正確呼叫參數`() = runTest {
        // Given
        val dayEpoch = 1_700_000_000L
        val day = FakeWeatherFactory.createDay("2025-10-29", dayEpoch)
        val timeline = FakeWeatherFactory.createTimeline(
            address = "Taipei,TW",
            days = listOf(day),
            current = Hour.DEFAULT
        )
        coEvery {
            repo.getWeather(location = any(), startDay = any(), endDay = any(), apiKey = any())
        } returns Resource.Success(timeline)

        // When
        val result = useCase("Taipei,TW", "2025-10-28", "2025-11-04", "dummy-key")

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.address).isEqualTo("Taipei,TW")
        assertThat(data.days).isNotEmpty()

        coVerify(exactly = 1) {
            repo.getWeather(
                location = "Taipei,TW",
                startDay = "2025-10-28",
                endDay = "2025-11-04",
                apiKey = "dummy-key"
            )
        }
    }

    /** EC2：Error → 轉交錯誤訊息 */
    @Test
    fun `EC2 - Repository 回傳 Error 應轉交 Error`() = runTest {
        // Given
        coEvery {
            repo.getWeather(location = any(), startDay = any(), endDay = any(), apiKey = any())
        } returns Resource.Error("network")

        // When
        val result = useCase("Taipei,TW", "2025-10-28", "2025-11-04", "dummy-key")

        // Then
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("network")
    }
}