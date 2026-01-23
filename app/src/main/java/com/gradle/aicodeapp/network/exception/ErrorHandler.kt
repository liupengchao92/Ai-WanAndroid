package com.gradle.aicodeapp.network.exception

import com.gradle.aicodeapp.utils.ToastUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ErrorHandler {

    private val _errorEvent = MutableSharedFlow<ErrorEvent>(replay = 1, extraBufferCapacity = 1)
    val errorEvent = _errorEvent.asSharedFlow()

    data class ErrorEvent(
        val code: Int,
        val message: String,
        val type: ErrorType
    )

    enum class ErrorType {
        NOT_LOGIN,
        COLLECT_NOT_LOGIN,
        NETWORK,
        TIMEOUT,
        SERVER,
        PARSE,
        UNKNOWN
    }

    fun handleError(throwable: Throwable, requestUrl: String? = null) {
        val errorEvent = when (throwable) {
            is NotLoginException -> {
                ErrorEvent(
                    code = throwable.code,
                    message = throwable.message ?: "未登录或登录已过期",
                    type = ErrorType.NOT_LOGIN
                )
            }
            is ApiException -> {
                when (throwable.code) {
                    ErrorCode.ERROR_TIMEOUT -> ErrorEvent(
                        code = throwable.code,
                        message = "请求超时，请稍后重试",
                        type = ErrorType.TIMEOUT
                    )
                    ErrorCode.ERROR_NETWORK -> ErrorEvent(
                        code = throwable.code,
                        message = "网络连接失败，请检查网络设置",
                        type = ErrorType.NETWORK
                    )
                    ErrorCode.ERROR_PARSE -> ErrorEvent(
                        code = throwable.code,
                        message = "数据解析失败",
                        type = ErrorType.PARSE
                    )
                    else -> ErrorEvent(
                        code = throwable.code,
                        message = throwable.message ?: "请求失败",
                        type = ErrorType.SERVER
                    )
                }
            }
            is java.net.SocketTimeoutException -> {
                ErrorEvent(
                    code = ErrorCode.ERROR_TIMEOUT,
                    message = "请求超时，请稍后重试",
                    type = ErrorType.TIMEOUT
                )
            }
            is java.net.UnknownHostException -> {
                ErrorEvent(
                    code = ErrorCode.ERROR_NETWORK,
                    message = "网络连接失败，请检查网络设置",
                    type = ErrorType.NETWORK
                )
            }
            is java.io.IOException -> {
                ErrorEvent(
                    code = ErrorCode.ERROR_NETWORK,
                    message = "网络异常，请稍后重试",
                    type = ErrorType.NETWORK
                )
            }
            else -> {
                ErrorEvent(
                    code = ErrorCode.ERROR_UNKNOWN,
                    message = throwable.message ?: "未知错误",
                    type = ErrorType.UNKNOWN
                )
            }
        }

        _errorEvent.tryEmit(errorEvent)
    }

    fun emitErrorEvent(errorEvent: ErrorEvent) {
        _errorEvent.tryEmit(errorEvent)
    }

    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is NotLoginException -> "未登录或登录已过期"
            is ApiException -> {
                when (throwable.code) {
                    ErrorCode.ERROR_TIMEOUT -> "请求超时，请稍后重试"
                    ErrorCode.ERROR_NETWORK -> "网络连接失败，请检查网络设置"
                    ErrorCode.ERROR_PARSE -> "数据解析失败"
                    else -> throwable.message ?: "请求失败"
                }
            }
            is java.net.SocketTimeoutException -> "请求超时，请稍后重试"
            is java.net.UnknownHostException -> "网络连接失败，请检查网络设置"
            is java.io.IOException -> "网络异常，请稍后重试"
            else -> throwable.message ?: "未知错误"
        }
    }

    fun showToast(message: String) {
        ToastUtils.showShort(message)
    }
}
