package com.alex.yang.weather.core.network

import android.util.MalformedJsonException
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
suspend inline fun <reified T, R> safeApiCall(
    crossinline request: suspend () -> Response<T>,
    crossinline response: suspend (T) -> Resource<R>
): Resource<R> {
    return try {
        val apiResponse = request()

        when {
            apiResponse.isSuccessful -> {
                apiResponse.body()
                    ?.let { response(it) }
                    ?: Resource.Error("[UNKNOWN] 請稍後再重試")
            }
            else -> {
                Resource.Error(
                    when (apiResponse.code()) {
                        400 -> "[400] 錯誤的請求參數 (Bad Request)"
                        401 -> "[401] 未授權或 API 金鑰錯誤 (Unauthorized)"
                        403 -> "[403] 拒絕存取，請檢查授權設定 (Forbidden)"
                        404 -> "[404] 查無資料 (Not Found)"
                        429 -> "[429] 超過呼叫次數限制 (Too Many Requests)"
                        500 -> "[500] 伺服器錯誤 (Internal Server Error)"
                        503 -> "[503] 服務暫時無法使用 (Service Unavailable)"
                        else -> "[${apiResponse.code()}] 請稍後再重試"
                    }
                )
            }
        }
    } catch (e: Exception) {
        Resource.Error(
            when (e) {
                is SerializationException,
                is MalformedJsonException ->
                    "[PARSE] 資料解析失敗，請稍後再重試"

                is ConnectException,
                is SocketTimeoutException,
                is UnknownHostException ->
                    "[NETWORK] 無法連線伺服器，請稍後再重試"

                is SSLException -> "[SSL] 安全連線失敗，請稍後再重試"
                else -> "[UNKNOWN] ${e.message}"
            }
        )
    }
}