package com.alex.yang.weather.data.datasource

import com.google.android.gms.tasks.Tasks
import com.google.common.truth.Truth.assertThat
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

/**
 * Created by AlexYang on 2025/10/31.
 *
 * 測試目標：
 * - 覆蓋 RemoteConfigDataSourceImpl 封裝的委派行為：
 *   fetchAndActivate() 以 await() 取得結果；getString(key) 直接轉呼叫 RC。
 *
 * 測試範圍：
 * 1) fetchAndActivate() 成功回傳 true
 * 2) fetchAndActivate() 成功但回傳 false
 * 3) fetchAndActivate() 內部 Task 失敗/拋例外 → 應將例外向上拋出
 * 4) getString(key) 能正確回傳遠端值
 * 5) getString(key) 未設定時回傳空字串
 *
 * 覆蓋情境（Test Cases）：
 *  EC1  fetchAndActivate 成功 → 回傳 true
 *  EC2  fetchAndActivate 成功但為 false → 回傳 false
 *  EC3  fetchAndActivate 例外 → 應拋出例外（不吞例外）
 *  EC4  getString 取得 "api_key" → 回傳 "REMOTE_KEY"
 *  EC5  getString 取得不存在的 key → 回傳空字串
 */
class RemoteConfigDataSourceTest {

    @Test
    fun `EC1 - fetchAndActivate_whenTaskSuccessTrue_returnsTrue`() = runTest {
        // Given
        val remoteConfig = mockk<FirebaseRemoteConfig>()
        every { remoteConfig.fetchAndActivate() } returns Tasks.forResult(true)

        val dataSource = RemoteConfigDataSourceImpl(remoteConfig)

        // When
        val result = dataSource.fetchAndActivate()

        // Then
        assertThat(result).isTrue()
        verify(exactly = 1) { remoteConfig.fetchAndActivate() }
    }

    @Test
    fun `EC2 - fetchAndActivate_whenTaskSuccessFalse_returnsFalse`() = runTest {
        // Given
        val remoteConfig = mockk<FirebaseRemoteConfig>()
        every { remoteConfig.fetchAndActivate() } returns Tasks.forResult(false)

        val dataSource = RemoteConfigDataSourceImpl(remoteConfig)

        // When
        val result = dataSource.fetchAndActivate()

        // Then
        assertThat(result).isFalse()
        verify(exactly = 1) { remoteConfig.fetchAndActivate() }
    }

    @Test
    fun `EC3 - fetchAndActivate_whenTaskFails_shouldThrow`() = runTest {
        // Given
        val remoteConfig = mockk<FirebaseRemoteConfig>()
        every { remoteConfig.fetchAndActivate() } returns Tasks.forException(RuntimeException("rc failed"))

        val dataSource = RemoteConfigDataSourceImpl(remoteConfig)

        // Then（When 內含）
        assertFailsWith<RuntimeException> {
            dataSource.fetchAndActivate()
        }
        verify(exactly = 1) { remoteConfig.fetchAndActivate() }
    }

    @Test
    fun `EC4 - getString_whenKeyExists_returnsValue`() {
        // Given
        val remoteConfig = mockk<FirebaseRemoteConfig>()
        every { remoteConfig.getString("api_key") } returns "REMOTE_KEY"

        val dataSource = RemoteConfigDataSourceImpl(remoteConfig)

        // When
        val result = dataSource.getString("api_key")

        // Then
        assertThat(result).isEqualTo("REMOTE_KEY")
        verify(exactly = 1) { remoteConfig.getString("api_key") }
    }

    @Test
    fun `EC5 - getString_whenKeyMissing_returnsEmptyString`() {
        // Given
        val remoteConfig = mockk<FirebaseRemoteConfig>()
        every { remoteConfig.getString("missing_key") } returns ""

        val dataSource = RemoteConfigDataSourceImpl(remoteConfig)

        // When
        val result = dataSource.getString("missing_key")

        // Then
        assertThat(result).isEmpty()
        verify(exactly = 1) { remoteConfig.getString("missing_key") }
    }
}