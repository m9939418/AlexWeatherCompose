@file:Suppress("NonAsciiCharacters")

package com.alex.yang.weather.data.repository

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
import com.alex.yang.weather.data.local.AppPreferences
import com.alex.yang.weather.data.local.MainDispatcherRule
import com.google.android.gms.tasks.Tasks
import com.google.common.truth.Truth.assertThat
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

/**
 * Created by AlexYang on 2025/10/30.
 *
 * 單元測試：針對 ConfigRepository 進行測試（Robolectric + 注入 Test DataStore）
 *
 * 測試重點：
 * - 成功：fetchAndActivate 成功且取得遠端字串 → 寫入 AppPreferences
 * - 空字串：遠端 countiesJson 為空 → 回退 assets
 * - 失敗：fetchAndActivate 拋出例外 → 不應崩潰，並保持預設
 *
 * 測試覆蓋情境
 *  EC    測試情境                                      預期行為
 * |-----|---------------------------------------------|----------------------------------------------|
 *  EC1  fetch 成功 + 取得 api_key 與 countiesJson       | apiKey = REMOTE_KEY 且 countiesJson 包含 "counties"
 *  EC2  fetch 成功 + countiesJson 空字串 → fallback     | countiesJson 非空（來源為 assets）
 *  EC3  fetch 拋出例外 → 不應崩潰，保持預設值          | apiKey 為預設 key，countiesJson 為空或預設
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ConfigRepositoryTest {
    @get:Rule
    val tmp: TemporaryFolder = TemporaryFolder()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    /**
     * EC1：fetch 成功 + 取得 api_key 與 countiesJson → 應寫入 Preferences
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `EC1 - fetch 成功且取得遠端字串 應寫入到偏好儲存`() = runTest {
        // Given
        val rc = mockk<FirebaseRemoteConfig>(relaxed = true)
        every { rc.fetchAndActivate() } returns Tasks.forResult(true)
        every { rc.getString("api_key") } returns "REMOTE_KEY"
        every { rc.getString("tw_counties_v1") } returns """{"version":1,"updatedAt":"now","counties":[]}"""

        // 注入「獨立檔案」的 DataStore，避免汙染其他測試
        val prefsFile = tmp.newFile("prefs_config_ec1.preferences_pb")
        val testStore = PreferenceDataStoreFactory.create { prefsFile }
        val prefs = AppPreferences(context, json, testStore)

        val repo = ConfigRepository(context, rc, prefs, json)

        // When
        repo.fetchConfig()
        advanceUntilIdle()

        // Then
        io.mockk.verify(exactly = 1) { rc.fetchAndActivate() }
        io.mockk.verify { rc.getString("api_key") }
        io.mockk.verify { rc.getString("tw_counties_v1") }

        val apiKey = prefs.getApiKey().first()
        val countiesJson = prefs.getCountiesJson().first()

        assertThat(apiKey).isEqualTo("REMOTE_KEY")
        val root = json.parseToJsonElement(countiesJson).jsonObject
        assertThat(root.containsKey("counties")).isTrue()
    }

    /**
     * EC2：遠端 countiesJson 空字串 → 應回退 assets
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `EC2 - 遠端 countiesJson 為空 應回退資產檔`() = runTest {
        // Given
        val rc = mockk<FirebaseRemoteConfig>(relaxed = true)
        every { rc.fetchAndActivate() } returns Tasks.forResult(true)
        every { rc.getString("api_key") } returns "KEY"
        every { rc.getString("tw_counties_v1") } returns ""

        val prefsFile = tmp.newFile("prefs_config_ec2.preferences_pb")
        val testStore = PreferenceDataStoreFactory.create { prefsFile }
        val prefs = AppPreferences(context, json, testStore)

        val repo = ConfigRepository(context, rc, prefs, json)

        // When
        repo.fetchConfig()
        advanceUntilIdle()

        // Then
        val apiKey = prefs.getApiKey().first()
        val countiesJson = prefs.getCountiesJson().first()

        assertThat(apiKey).isEqualTo("KEY")
        val root = json.parseToJsonElement(countiesJson).jsonObject
        assertThat(root.containsKey("counties")).isTrue()
    }

    /**
     * EC3：fetch 拋出例外 → 不應崩潰，保持預設值
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `EC3 - fetch 拋出例外 應不崩潰且保持預設`() = runTest {
        // Given
        val rc = mockk<FirebaseRemoteConfig>(relaxed = true)
        every { rc.fetchAndActivate() } returns Tasks.forException(RuntimeException("network fail"))

        val prefsFile = tmp.newFile("prefs_config_ec3.preferences_pb")
        val prefs = AppPreferences(context, json, PreferenceDataStoreFactory.create { prefsFile })
        val repo = ConfigRepository(context, rc, prefs, json)

        // When
        repo.fetchConfig()
        advanceUntilIdle()

        // Then
        val apiKey = prefs.getApiKey().first()
        val countiesJson = prefs.getCountiesJson().first()
        assertThat(apiKey).isNotEmpty()
        assertThat(countiesJson).isNotNull()
    }
}