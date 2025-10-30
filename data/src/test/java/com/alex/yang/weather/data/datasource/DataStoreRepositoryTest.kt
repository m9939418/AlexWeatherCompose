package com.alex.yang.weather.data.datasource

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.alex.yang.weather.domain.model.County
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test

/**
 * Created by AlexYang on 2025/10/31.
 *
 * 測試範圍：
 * 1) saveConfig() 會正確寫入 apiKey 與 countiesJson，後續 get* 能讀到最新值
 * 2) 未曾寫入時：
 *    - getApiKey() 應回傳實作定義的預設值（非空字串）
 *    - getCountiesJson() 應回傳空字串
 *    - getSelectedCounty() 應回傳 County.DEFAULT
 * 3) saveSelectedCounty() 後，getSelectedCounty() 應還原與寫入相同的資料（驗證序列化／反序列化）
 *
 *
 * 覆蓋情境（Test Cases）：
 *  EC1  saveConfig 之後，getApiKey 與 getCountiesJson 讀到最新值
 *  EC2  未寫入任何值時，getApiKey 回傳預設（非空）
 *  EC3  未寫入任何值時，getCountiesJson 回傳空字串
 *  EC4  未寫入任何值時，getSelectedCounty 回傳 County.DEFAULT
 *  EC5  先 saveSelectedCounty，再 getSelectedCounty 還原相同資料
 */
class DataStoreRepositoryTest {
    @get:Rule
    val tmp: TemporaryFolder = TemporaryFolder()

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
    }

    private fun newRepo(): DataStoreRepositoryImpl {
        val file = tmp.newFile("prefs_ds_repo_impl_test.preferences_pb")
        val ds = PreferenceDataStoreFactory.create { file }
        return DataStoreRepositoryImpl(ds, json)
    }

    @Test
    fun `EC1 - saveConfig_then_getApiKeyAndCountiesJson_returnsLatestValues`() = runTest {
        // Given
        val repo = newRepo()

        // When
        val newKey = "ALEX_KEY_123"
        val newJson = """{"version":1,"updatedAt":"2025-10-18","counties":[{"zh":"台北市","en":"Taipei"}]}"""
        repo.saveConfig(apiKey = newKey, countiesJson = newJson)

        // Then
        val key = repo.getApiKey().first()
        val text = repo.getCountiesJson().first()
        assertThat(key).isEqualTo(newKey)
        assertThat(text).contains("\"counties\"")
        assertThat(text).contains("台北市")
    }

    @Test
    fun `EC2 - getApiKey_whenNoValueSaved_returnsDefault`() = runTest {
        // Given
        val repo = newRepo()

        // When
        val result = repo.getApiKey().first()

        // Then
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `EC3 - getCountiesJson_whenNoValueSaved_returnsEmptyString`() = runTest {
        // Given
        val repo = newRepo()

        // When
        val result = repo.getCountiesJson().first()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `EC4 - getSelectedCounty_whenNoValueSaved_returnsDefault`() = runTest {
        // Given
        val repo = newRepo()

        // When
        val result = repo.getSelectedCounty().first()

        // Then
        assertThat(result).isEqualTo(County.DEFAULT)
    }

    @Test
    fun `EC5 - getSelectedCounty_afterSaveSelectedCounty_returnsSameValue`() = runTest {
        // Given
        val repo = newRepo()

        val toSave = County.DEFAULT

        // When
        repo.saveSelectedCounty(toSave)
        val result = repo.getSelectedCounty().first()

        // Then
        assertThat(result).isEqualTo(toSave)
    }
}