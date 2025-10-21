package com.alex.yang.weather.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/10/20.
 *
 * 檢查網路連線狀態
 *
 * 以 INTERNET + VALIDATED 為準，並對 activeNetwork 失敗時 fallback allNetworks
 */
class NetworkManager @Inject constructor(
    private val context: Context
) {
    private val connectManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    suspend inline fun <R> guardOnline(
        crossinline doOnAvailable: suspend () -> R,
        crossinline doOnLost: suspend () -> R
    ): R {
        return if (isNetworkConnected()) doOnAvailable() else doOnLost()
    }

    /** 是否真的可連外（避免誤判：需登入 Wi-Fi / 本地網） */
    fun isNetworkConnected(): Boolean {
        // 1. 先檢查預設路由
        connectManager.activeNetwork?.let { network ->
            connectManager.getNetworkCapabilities(network)?.let { caps ->
                if (caps.isReallyOnline()) return true
            }
        }

        // 2. fallback：逐一檢查所有 Network
        for (n in connectManager.allNetworks) {
            val caps = connectManager.getNetworkCapabilities(n) ?: continue
            if (caps.isReallyOnline()) return true
        }

        return false
    }

    private fun NetworkCapabilities.isReallyOnline(): Boolean {
        val hasInternet = hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val isValidated = hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        return hasInternet && isValidated && isAcceptableTransport()
    }

    private fun NetworkCapabilities.isAcceptableTransport(): Boolean {
        return hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}