package com.alex.yang.home.util

import com.alex.yang.weather.core.network.NetworkStateDetect

/**
 * Created by AlexYang on 2025/10/31.
 *
 *
 */
class MockNetworkStateDetectImpl(var online: Boolean = true) : NetworkStateDetect {
    override suspend fun <R> guardOnline(
        doOnAvailable: suspend () -> R,
        doOnLost: suspend () -> R
    ): R = if (online) doOnAvailable() else doOnLost()
}