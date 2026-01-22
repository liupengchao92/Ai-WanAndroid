package com.gradle.aicodeapp.network.exception

import com.gradle.aicodeapp.utils.LogUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ErrorLogger {

    private const val TAG = "NetworkError"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun log(
        errorCode: Int,
        errorMsg: String,
        requestUrl: String? = null,
        requestParams: String? = null,
        throwable: Throwable? = null
    ) {
        val timestamp = dateFormat.format(Date())
        val logMessage = buildString {
            appendLine("========== Network Error ==========")
            appendLine("Timestamp: $timestamp")
            appendLine("Error Code: $errorCode")
            appendLine("Error Message: $errorMsg")
            requestUrl?.let { appendLine("Request URL: $it") }
            requestParams?.let { appendLine("Request Params: $it") }
            throwable?.let { appendLine("Exception: ${it.javaClass.simpleName}") }
            appendLine("=====================================")
        }

        if (throwable != null) {
            LogUtils.e(TAG, logMessage, throwable)
        } else {
            LogUtils.e(TAG, logMessage)
        }
    }

    fun logNetworkError(
        requestUrl: String,
        throwable: Throwable
    ) {
        val errorCode = when (throwable) {
            is java.net.SocketTimeoutException -> ErrorCode.ERROR_TIMEOUT
            is java.net.UnknownHostException -> ErrorCode.ERROR_NETWORK
            else -> ErrorCode.ERROR_NETWORK
        }
        val errorMsg = when (throwable) {
            is java.net.SocketTimeoutException -> "请求超时"
            is java.net.UnknownHostException -> "网络连接失败"
            else -> "网络异常: ${throwable.message}"
        }

        log(
            errorCode = errorCode,
            errorMsg = errorMsg,
            requestUrl = requestUrl,
            throwable = throwable
        )
    }

    fun logApiError(
        errorCode: Int,
        errorMsg: String,
        requestUrl: String? = null,
        requestParams: String? = null
    ) {
        log(
            errorCode = errorCode,
            errorMsg = errorMsg,
            requestUrl = requestUrl,
            requestParams = requestParams
        )
    }

    fun logNotLoginError(
        requestUrl: String? = null,
        requestParams: String? = null
    ) {
        log(
            errorCode = ErrorCode.ERROR_NOT_LOGIN,
            errorMsg = "未登录或登录已过期",
            requestUrl = requestUrl,
            requestParams = requestParams
        )
    }
}
