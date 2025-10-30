package com.alex.yang.weather.domain.model

import kotlinx.serialization.Serializable

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
@Serializable
data class County(
    val zh: String,
    val en: String
) {
    companion object {
        val DEFAULT = County(zh = "台北市", en = "Taipei")
    }
}
