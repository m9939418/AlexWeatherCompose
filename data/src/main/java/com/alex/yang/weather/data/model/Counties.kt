package com.alex.yang.weather.data.model

import kotlinx.serialization.Serializable

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
@Serializable
data class Counties(
    val version: Int,
    val updatedAt: String,
    val counties: List<County>
)
