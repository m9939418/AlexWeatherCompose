@file:Suppress("NonAsciiCharacters")

package com.alex.yang.weather.data.local

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
import com.alex.yang.weather.data.model.County
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.model.Statement
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test


/**
 * Created by AlexYang on 2025/10/30.
 *
 * 單元測試：針對 AppPreferences（DataStore）進行測試
 *
 * 測試重點：
 * - 儲存 / 讀取 apiKey
 * - 儲存 / 讀取使用者選擇的 County
 * - 儲存 / 讀取 countiesJson（供後續解析）
 *
 * 測試覆蓋情境
 *  EC    測試情境                                   預期行為
 * |-----|----------------------------------------|--------------------------------------|
 *  EC1  儲存 apiKey 與 County 後讀取               | 讀回值與儲存一致（apiKey、County）
 *  EC2  未曾儲存 apiKey（使用預設值）               | 讀回非空字串（具備預設 key）
 *  EC3  儲存 countiesJson 後讀取                   | 反序列化後驗證 version 與 counties
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppPreferencesTest {
    @get:Rule
    val tmp: TemporaryFolder = TemporaryFolder()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    /**
     * EC1：儲存 apiKey 與 County 後讀取 → 應與儲存一致
     */
    @Test
    fun `EC1 - 儲存 apiKey 與 County 後讀取 應與儲存一致`() = runTest {
        // Given
        val prefsFile = tmp.newFile("prefs_ec1.preferences_pb")
        val testStore = PreferenceDataStoreFactory.create { prefsFile }
        val prefs = AppPreferences(context, json, testStore)
        val expectedKey = "TEST_KEY_123"
        val expectedCounty = County(zh = "台中市", en = "Taichung")

        // When
        prefs.saveConfig(
            apiKey = expectedKey,
            countiesJson = """{"version":1,"updatedAt":"now","counties":[]}"""
        )
        prefs.saveSelectedCounty(expectedCounty)

        val actualKey = prefs.getApiKey().first()
        val actualCounty = prefs.getSelectedCounty().first()

        // Then
        assertThat(actualKey).isEqualTo(expectedKey)
        assertThat(actualCounty.zh).isEqualTo(expectedCounty.zh)
        assertThat(actualCounty.en).isEqualTo(expectedCounty.en)
    }

    /**
     * EC2：未曾儲存 apiKey（使用預設值） → 應讀回非空字串
     */
    @Test
    fun `EC2 - 未曾儲存 apiKey 應讀回非空預設值`() = runTest {
        // Given
        val prefsFile = tmp.newFile("prefs_ec2.preferences_pb")
        val testStore = PreferenceDataStoreFactory.create { prefsFile }
        val prefs = AppPreferences(context, json, testStore)

        // When
        val actualKey = prefs.getApiKey().first()

        // Then
        assertThat(actualKey).isNotEmpty() // 預設 key：W4GXC3K9XX7JV9DJ99BCS5Z69
    }

    /**
     * EC3：儲存 countiesJson 後讀取 → 反序列化驗證欄位
     */
    @Test
    fun `EC3 - 儲存 countiesJson 後讀取 應包含版本與縣市欄位`() = runTest {
        // Given
        val prefsFile = tmp.newFile("prefs_ec3.preferences_pb")
        val testStore = PreferenceDataStoreFactory.create { prefsFile }
        val prefs = AppPreferences(context, json, testStore)
        val expectedJson = """
            {
              "version": 2,
              "updatedAt": "2025-10-30T00:00:00Z",
              "counties": [
                {"zh":"台北市","en":"Taipei"},
                {"zh":"台中市","en":"Taichung"}
              ]
            }
        """.trimIndent()

        // When
        prefs.saveConfig(apiKey = "DUMMY", countiesJson = expectedJson)
        val actualJson = prefs.getCountiesJson().first()

        // Then
        val obj = json.decodeFromString<CountiesConfig>(actualJson)
        assertThat(obj.version).isEqualTo(2)
        assertThat(obj.counties.map { it.en }).containsAtLeast("Taipei", "Taichung")
        assertThat(obj.counties.size).isEqualTo(2)
    }
}


/** 測試時用的 JSON model（只為了強化 EC3 斷言） */
@Serializable
data class CountiesConfig(
    val version: Int,
    @SerialName("updatedAt") val updatedAt: String? = null,
    val counties: List<CountyItem>
)

@Serializable
data class CountyItem(val zh: String, val en: String)

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(dispatcher)
                try {
                    base.evaluate()
                } finally {
                    Dispatchers.resetMain()
                }
            }
        }
    }
}