package com.alex.yang.home.presentation

import com.alex.yang.home.util.fakeDay
import com.alex.yang.home.util.fakeHour
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Created by AlexYang on 2025/10/31.
 *
 * 單元測試：針對 HomeSharedViewModel（共享資料）進行測試
 *
 * 測試目標：
 * - 驗證 setHours / setDays 會正確更新對應的 StateFlow（行為聚焦，不依賴 Android 環境）
 *
 * 測試範圍：
 * 1) 初始狀態應為空集合
 * 2) 設定非空資料 → 觀察到相同清單
 * 3) 重複設定 → 以最後一次設定為準（last wins）
 * 4) 設定空清單 → 能正確發出空集合（Empty State）
 *
 * 覆蓋情境（EC, Equivalence Class）：
 *  EC0  初始狀態：hours/days 為空集合
 *  EC1  setHours 非空 → 觀察到相同清單
 *  EC2  setDays 非空 → 觀察到相同清單
 *
 */
class HomeSharedViewModelTest {
    @Test
    fun `EC0 - default_state_isEmptyLists`() = runTest {
        // Given
        val viewModel = HomeSharedViewModel()

        // Then
        assertThat(viewModel.hours.first()).isEmpty()
        assertThat(viewModel.days.first()).isEmpty()
    }

    @Test
    fun `EC1 - setHours_then_stateFlowEmitsSameList`() = runTest {
        // Given
        val viewModel = HomeSharedViewModel()

        val hours = listOf(
            fakeHour(datetime = "10:00", epoch = 10_000, temp = 20),
            fakeHour(datetime = "11:00", epoch = 13_600, temp = 21),
        )
        // When
        viewModel.setHours(hours)

        // Then
        val result = viewModel.hours.first()
        assertThat(result).isEqualTo(hours)
    }

    @Test
    fun `EC2 - setDays_then_stateFlowEmitsSameList`() = runTest {
        // Given
        val viewModel = HomeSharedViewModel()

        val days = listOf(
            fakeDay(date = "2025-10-31", epoch = 1_000, tmax = 26, tmin = 20),
            fakeDay(date = "2025-11-01", epoch = 1_000 + 86_400, tmax = 27, tmin = 21),
        )

        // When
        viewModel.setDays(days)

        // Then
        val result = viewModel.days.first()
        assertThat(result).isEqualTo(days)
    }

}