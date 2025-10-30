@file:Suppress("KDocUnresolvedReference", "NonAsciiCharacters")

package com.alex.yang.weather.data.repository

import com.alex.yang.weather.core.network.Resource
import com.alex.yang.weather.data.api.VisualCrossingWebServices
import com.alex.yang.weather.data.model.DayDto
import com.alex.yang.weather.data.model.HourDto
import com.alex.yang.weather.data.model.TimelineDto
import com.alex.yang.weather.domain.repository.WeatherRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.test.Test

/**
 * Created by AlexYang on 2025/10/30.
 *
 * 單元測試：針對 WeatherRepositoryImpl 進行測試
 *
 * 測試重點：
 * - 成功：HTTP 200 + 有 body → 回傳 Resource.Success 並完成 DTO→Domain 映射
 * - 失敗（空 body）：HTTP 200 + body=null → 回傳 Resource.Error([UNKNOWN] ...)
 * - 失敗（HTTP 非 2xx）：回傳對應錯誤訊息（以 404 為例）
 * - 失敗（網路例外）：UnknownHostException → [NETWORK] 錯誤訊息
 * - 失敗（解析例外）：SerializationException → [PARSE] 錯誤訊息
 *
 * 測試覆蓋情境
 *  EC    測試情境                                              預期行為
 * |-----|---------------------------------------------------|-----------------------------------------------|
 *  EC1  200 + 有 body                                       | Resource.Success，且 address 與 days 正確
 *  EC2  200 + body=null                                     | Resource.Error，訊息含 [UNKNOWN]
 *  EC3  404 Not Found                                       | Resource.Error，訊息含 [404] 查無資料
 *  EC4  網路例外（UnknownHostException）                     | Resource.Error，訊息含 [NETWORK]
 *  EC5  解析例外（SerializationException）                   | Resource.Error，訊息含 [PARSE]
 */
class WeatherRepositoryImplTest {
    private val api = mockk<VisualCrossingWebServices>()
    private val repository: WeatherRepository = WeatherRepositoryImpl(api)

    /**
     * EC1：200 + 有 body → Success
     */
    @Test
    fun `EC1 - 200 + 有 body 應回傳 Resource_Success`() = runTest {
        // Given
        val dto = TimelineDto(
            queryCost = 1,
            address = "Taipei,TW",
            days = listOf(
                DayDto(datetime = "2025-10-16", hours = listOf(HourDto(datetime = "10:00:00")))
            ),
            currentConditions = HourDto(datetime = "10:00:00")
        )
        coEvery {
            api.getWeather(any(), any(), any(), any(), any(), any(), any())
        } returns Response.success(dto)

        // When
        val result = repository.getWeather("Taipei,TW", "2025-10-16", "2025-10-17", "KEY")

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.address).isEqualTo("Taipei,TW")
        assertThat(data.days).isNotEmpty()
        assertThat(data.currentConditions).isNotNull()
    }

    /**
     * EC2：200 + body = null → Error [UNKNOWN]
     */
    @Test
    fun `EC2 - 200 + body 為 null 應回傳 Resource_Error_UNKNOWN`() = runTest {
        // Given
        coEvery {
            api.getWeather(any(), any(), any(), any(), any(), any(), any())
        } returns Response.success(null)

        // When
        val result = repository.getWeather("Taipei,TW", "2025-10-16", "2025-10-17", "KEY")

        // Then
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val msg = (result as Resource.Error).message
        assertThat(msg).contains("[UNKNOWN]")
    }

    /**
     * EC3：HTTP 404 → Error [404] 查無資料
     */
    @Test
    fun `EC3 - 404 Not Found 應回傳 Resource_Error_404`() = runTest {
        // Given
        val data = """{"error":"not found"}""".toResponseBody("application/json".toMediaType())
        coEvery {
            api.getWeather(any(), any(), any(), any(), any(), any(), any())
        } returns Response.error(404, data)

        // When
        val result = repository.getWeather("Taipei,TW", "2025-10-16", "2025-10-17", "KEY")

        // Then
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val msg = (result as Resource.Error).message
        assertThat(msg).contains("[404] 查無資料")
    }

    /**
     * EC4：UnknownHostException → Error [NETWORK]
     */
    @Test
    fun `EC4 - 網路例外 UnknownHostException 應回傳 Resource_Error_NETWORK`() = runTest {
        // Given
        coEvery {
            api.getWeather(any(), any(), any(), any(), any(), any(), any())
        } throws UnknownHostException("no internet")

        // When
        val result = repository.getWeather("Taipei,TW", "2025-10-16", "2025-10-17", "KEY")

        // Then
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val msg = (result as Resource.Error).message
        assertThat(msg).contains("[NETWORK] 無法連線伺服器")
    }

    /**
     * EC5：SerializationException → Error [PARSE]
     */
    @Test
    fun `EC5 - 解析例外 SerializationException 應回傳 Resource_Error_PARSE`() = runTest {
        // Given
        coEvery {
            api.getWeather(any(), any(), any(), any(), any(), any(), any())
        } throws SerializationException("bad json")

        // When
        val result = repository.getWeather("Taipei,TW", "2025-10-16", "2025-10-17", "KEY")

        // Then
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        val msg = (result as Resource.Error).message
        assertThat(msg).contains("[PARSE] 資料解析失敗")
    }
}