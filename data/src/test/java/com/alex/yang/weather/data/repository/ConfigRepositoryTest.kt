@file:Suppress("NonAsciiCharacters")

package com.alex.yang.weather.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.alex.yang.weather.data.datasource.AssetsLocalDataSource
import com.alex.yang.weather.data.datasource.DataStoreRepositoryImpl
import com.alex.yang.weather.data.datasource.RemoteConfigDataSource
import com.alex.yang.weather.domain.repository.DataStoreRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

/**
 * Created by AlexYang on 2025/10/30.
 *
 * 單元測試：針對 ConfigRepositoryImpl 進行測試（以 Preference DataStore 建測試用儲存，Robolectric Runner）
 *
 * 測試重點（符合實作邏輯）：
 * 1) 成功：RemoteConfig.fetchAndActivate 成功，且拿到非空的
 *    - api_key
 *    - tw_counties_v1（縣市 JSON）
 *    → 兩者都應寫入 DataStore
 *
 * 2) 空字串：tw_counties_v1 為空字串 → 應回退使用 AssetsLocalDataSource.load("tw_counties.json")
 *    → 寫入 DataStore 的 countiesJson 來自 assets（非空且含 "counties"）
 *
 * 3) 失敗：fetchAndActivate 或流程中拋出例外 → 走 onFailure 回退：
 *    - apiKey：使用 DataStore 既有值（若尚未有，則為 DataStoreRepositoryImpl.DEFAULT_API_KEY）
 *    - countiesJson：使用 assets（tw_counties.json）
 *    → 不應崩潰
 *
 * 4) getCounties()：當 DataStore 中 countiesJson 為空時 → 應回退 assets 並成功解碼為 Counties
 *    （需包含 root 欄位 version / updatedAt 與陣列 counties，且 county 需具備 zh / en 等必要欄位）
 *
 * 測試覆蓋情境
 *  EC    測試情境                                              預期行為
 * |-----|-----------------------------------------------------|----------------------------------------------|
 *  EC1  同步設定_當遠端有值_則寫入ApiKey與縣市至DataStore          | DataStore：apiKey = REMOTE_KEY；countiesJson 含 "counties"
 *  EC2  同步設定_當遠端縣市為空_則回退Assets                     | DataStore：apiKey = KEY；countiesJson 取自 assets（非空）
 *  EC3  同步設定_當遠端失敗_則回退既有ApiKey與Assets()            | DataStore：apiKey 為既有/預設；countiesJson 取自 assets
 *  EC4  取得縣市_當DataStore為空_則回退Assets並成功解碼           | Counties(version/updatedAt 正確；counties 非空且欄位齊全)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ConfigRepositoryTest {
    @get:Rule
    val tmp: TemporaryFolder = TemporaryFolder()

    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    private fun newPrefs(): DataStoreRepository {
        val file = tmp.newFile("prefs_config_test.preferences_pb")
        val dataStore = PreferenceDataStoreFactory.create { file }
        return DataStoreRepositoryImpl(dataStore, json)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `EC1 - 同步設定_當遠端有值_則寫入ApiKey與縣市至DataStore`() =
        runTest(StandardTestDispatcher()) {
            // Given
            val remote = mockk<RemoteConfigDataSource>()
            coEvery { remote.fetchAndActivate() } returns true
            every { remote.getString("api_key") } returns "REMOTE_KEY"
            every { remote.getString("tw_counties_v1") } returns """{"version":1,"counties":[]}"""

            val assets = mockk<AssetsLocalDataSource>()
            every { assets.readJson() } returns """{"version":1,"counties":[{"id":1}]}"""

            val prefs = newPrefs()
            val repo = ConfigRepositoryImpl(remote, assets, prefs, json)

            // When
            repo.fetchAndCache()
            advanceUntilIdle()

            // Then
            val savedKey = prefs.getApiKey().first()
            val savedJson = prefs.getCountiesJson().first()
            assertThat(savedKey).isEqualTo("REMOTE_KEY")
            assertThat(savedJson).contains("counties")
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `EC2 - 同步設定_當遠端縣市為空_則回退Assets`() = runTest(StandardTestDispatcher()) {
        // Given
        val remote = mockk<RemoteConfigDataSource>()
        coEvery { remote.fetchAndActivate() } returns true
        every { remote.getString("api_key") } returns "KEY"
        every { remote.getString("tw_counties_v1") } returns ""

        val assets = mockk<AssetsLocalDataSource>()
        every { assets.readJson() } returns """{"version":1,"counties":[{"id":9}]}"""

        val prefs = newPrefs()
        val repo = ConfigRepositoryImpl(remote, assets, prefs, json)

        // When
        repo.fetchAndCache()
        advanceUntilIdle()

        // Then
        val savedKey = prefs.getApiKey().first()
        val savedJson = prefs.getCountiesJson().first()
        assertThat(savedKey).isEqualTo("KEY")
        assertThat(savedJson).contains("counties")
        assertThat(savedJson).contains("\"id\":9")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `EC3 - 同步設定_當遠端失敗_則回退既有ApiKey與Assets`() =
        runTest(StandardTestDispatcher()) {
            // Given
            val remote = mockk<RemoteConfigDataSource>()
            coEvery { remote.fetchAndActivate() } throws RuntimeException("network fail")

            val assets = mockk<AssetsLocalDataSource>()
            every { assets.readJson() } returns """{"version":1,"counties":[{"id":42}]}"""

            val prefs = newPrefs()
            val repo = ConfigRepositoryImpl(remote, assets, prefs, json)

            // When
            repo.fetchAndCache()
            advanceUntilIdle()

            // Then
            val savedKey = prefs.getApiKey().first()
            val savedJson = prefs.getCountiesJson().first()
            assertThat(savedKey).isNotEmpty()
            assertThat(savedJson).contains("\"id\":42")
        }

    // EC4：DataStore 空 → getCounties() 回退 assets 並成功 decode（含 updatedAt）
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `EC4 - 取得縣市_當DataStore為空_則回退Assets並成功解碼`() =
        runTest(StandardTestDispatcher()) {
            // Given
            val remote = mockk<RemoteConfigDataSource>(relaxed = true)
            val assets = mockk<AssetsLocalDataSource>()
            every { assets.readJson() } returns """
            {
              "version": 1,
              "updatedAt": "2025-10-18",
              "counties": [
                {"zh": "台北市", "en": "Taipei"},
                {"zh": "新北市", "en": "New Taipei"}
              ]
            }
        """.trimIndent()

            val prefs = newPrefs()
            val repo = ConfigRepositoryImpl(remote, assets, prefs, json)

            // When
            val counties = repo.getCounties()

            // Then
            assertThat(counties.version).isEqualTo(1)
            assertThat(counties.updatedAt).isEqualTo("2025-10-18")
            assertThat(counties.counties).hasSize(2)
            assertThat(counties.counties.first().zh).isEqualTo("台北市")
            assertThat(counties.counties.first().en).isEqualTo("Taipei")
        }
}