package com.alex.yang.weather.core.network

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: String) : Resource<Nothing>
}