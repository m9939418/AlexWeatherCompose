package com.alex.yang.weather.data.datasource

import android.content.res.AssetManager
import androidx.datastore.core.IOException
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.ByteArrayInputStream
import kotlin.test.Test

/**
 * Created by AlexYang on 2025/10/31.
 *
 * 測試目標：
 * - 覆蓋 AssetsLocalDataSourceImpl 的 readJson() 行為，驗證檔案開啟、UTF-8 讀取與例外傳遞。
 *
 * 測試範圍：
 * 1) 檔案存在時，readJson() 應回傳 tw_counties.json 的 UTF-8 文字內容
 * 2) 會正確以 "tw_counties.json" 作為檔名呼叫 AssetManager.open()
 * 3) 當 AssetManager.open() 拋出例外時，readJson() 應將例外向上拋出（不吞例外）
 * 4) 含有非 ASCII 文字（中文）時，內容應被完整保留
 *
 * 覆蓋情境（Test Cases）：
 *  EC1  檔案存在 → 回傳內容（含 "counties"）
 *  EC2  呼叫正確檔名 "tw_counties.json"
 *  EC3  open() 拋 IOException → readJson() 也拋出
 *  EC4  非 ASCII（中文）內容 → 內容完整保留
 */
class AssetsLocalDataSourceImplTest {

    @Test
    fun `EC1 - readJson_whenFileExists_returnsUtf8Text`() {
        // Given
        val json = """
            {
              "version": 1,
              "updatedAt": "2025-10-18",
              "counties": [
                {"zh": "台北市", "en": "Taipei"},
                {"zh": "新北市", "en": "New Taipei"}
              ]
            }
        """.trimIndent()

        val assetManager = mockk<AssetManager>()
        every { assetManager.open("tw_counties.json") } returns ByteArrayInputStream(json.toByteArray(Charsets.UTF_8))

        val dataSource = AssetsLocalDataSourceImpl(assetManager)

        // When
        val result = dataSource.readJson()

        // Then
        assertThat(result).contains("\"counties\"")
        assertThat(result).contains("台北市")
        assertThat(result).contains("New Taipei")
    }

    @Test
    fun `EC2 - readJson_shouldOpenTwCountiesJson`() {
        // Given
        val assetManager = mockk<AssetManager>()
        every { assetManager.open(any()) } returns ByteArrayInputStream("{}".toByteArray())

        val dataSource = AssetsLocalDataSourceImpl(assetManager)

        // When
        dataSource.readJson()

        // Then
        verify(exactly = 1) { assetManager.open("tw_counties.json") }
    }

    @Test(expected = IOException::class)
    fun `EC3 - readJson_whenOpenThrowsIOException_shouldPropagate`() {
        // Given
        val assetManager = mockk<AssetManager>()
        every { assetManager.open("tw_counties.json") } throws IOException("assets not found")

        val dataSource = AssetsLocalDataSourceImpl(assetManager)

        // When
        dataSource.readJson() // Then：預期拋出 IOException
    }

    @Test
    fun `EC4 - readJson_whenContainsUnicode_preservesContent`() {
        // Given
        val json = """{"version":1,"updatedAt":"2025-10-18","counties":[{"zh":"澎湖縣","en":"Penghu"}]}"""
        val assetManager = mockk<AssetManager>()
        every { assetManager.open("tw_counties.json") } returns ByteArrayInputStream(json.toByteArray(Charsets.UTF_8))

        val dataSource = AssetsLocalDataSourceImpl(assetManager)

        // When
        val result = dataSource.readJson()

        // Then
        assertThat(result).contains("澎湖縣")
        assertThat(result).contains("Penghu")
    }
}