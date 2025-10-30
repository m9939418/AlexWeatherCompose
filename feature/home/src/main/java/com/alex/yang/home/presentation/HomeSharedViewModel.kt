package com.alex.yang.home.presentation

import androidx.lifecycle.ViewModel
import com.alex.yang.weather.domain.model.Day
import com.alex.yang.weather.domain.model.Hour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/19.
 *
 * 首頁 共用 ViewModel
 *
 * 負責在首頁相關的多個畫面之間共享天氣資料狀態，主要用於：
 * - 在首頁與詳細頁之間傳遞每小時天氣資料 ([Hour])
 * - 在首頁與詳細頁之間傳遞每週天氣資料 ([Day])
 */
@HiltViewModel
class HomeSharedViewModel @Inject constructor() : ViewModel() {
    private val _hours = MutableStateFlow<List<Hour>>(emptyList())
    val hours: StateFlow<List<Hour>> = _hours

    private val _days = MutableStateFlow<List<Day>>(emptyList())
    val days: StateFlow<List<Day>> = _days

    fun setHours(list: List<Hour>) {
        _hours.value = list
    }

    fun setDays(list: List<Day>) {
        _days.value = list
    }
}