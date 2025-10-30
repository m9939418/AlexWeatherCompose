package com.alex.yang.weather.data.datasource

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/10/30.
 *
 *
 */
interface RemoteConfigDataSource {
    suspend fun fetchAndActivate(): Boolean

    fun getString(key: String): String
}

@Singleton
class RemoteConfigDataSourceImpl @Inject constructor(
    private val config: FirebaseRemoteConfig
) : RemoteConfigDataSource {
    override suspend fun fetchAndActivate(): Boolean {
        return config.fetchAndActivate().await()
    }

    override fun getString(key: String): String {
        return config.getString(key)
    }
}