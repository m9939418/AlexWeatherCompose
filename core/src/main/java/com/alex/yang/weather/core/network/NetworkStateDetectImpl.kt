package com.alex.yang.weather.core.network

import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/31.
 *
 *
 */
class NetworkStateDetectImpl @Inject constructor(
    private val networkManager: NetworkManager
) : NetworkStateDetect {
    override suspend fun <R> guardOnline(
        doOnAvailable: suspend () -> R,
        doOnLost: suspend () -> R
    ): R = networkManager.guardOnline(doOnAvailable, doOnLost)
}