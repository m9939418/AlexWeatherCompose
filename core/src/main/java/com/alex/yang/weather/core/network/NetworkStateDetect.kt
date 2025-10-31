package com.alex.yang.weather.core.network

/**
 * Created by AlexYang on 2025/10/31.
 *
 * 目的：讓 ViewModel/UseCase 依賴抽象，測試用 Fake 實作切換 online/offline。
 */
interface NetworkStateDetect {
    suspend fun <R> guardOnline(
        doOnAvailable: suspend () -> R,
        doOnLost: suspend () -> R
    ): R
}