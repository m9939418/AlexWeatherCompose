package com.alex.yang.weather.demo.navigation

import kotlinx.serialization.Serializable

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
@Serializable
sealed interface AppRoute {
    @Serializable
    data object Home : AppRoute

    @Serializable
    data class Web(val url: String, val caption: String) : AppRoute

    @Serializable
    data class TodayDetail(val index: Int) : AppRoute

    @Serializable
    data class WeeklyDetail(val index: Int, val city: String) : AppRoute
}