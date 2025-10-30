package com.alex.yang.weather.data.datasource

import android.content.res.AssetManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/10/30.
 *
 *
 */
interface AssetsLocalDataSource {
    fun readJson(): String
}

@Singleton
class AssetsLocalDataSourceImpl @Inject constructor(
    private val assets: AssetManager
) : AssetsLocalDataSource {
    override fun readJson(): String {
        return assets.open("tw_counties.json").use { it.readBytes().toString(Charsets.UTF_8) }
    }
}