package com.gradle.aicodeapp.network.exception

import java.io.IOException

open class ApiException(
    val code: Int,
    message: String,
    cause: Throwable? = null
) : IOException(message, cause) {

    override val message: String
        get() = super.message ?: "Unknown error"

    fun isNotLogin(): Boolean = code == ErrorCode.ERROR_NOT_LOGIN

    fun isSuccess(): Boolean = code == ErrorCode.SUCCESS
}

class NotLoginException(message: String = "未登录或登录已过期") : ApiException(
    ErrorCode.ERROR_NOT_LOGIN,
    message
)

class NetworkException(message: String = "网络连接失败") : ApiException(
    ErrorCode.ERROR_NETWORK,
    message
)

class TimeoutException(message: String = "请求超时") : ApiException(
    ErrorCode.ERROR_TIMEOUT,
    message
)

class ServerException(code: Int, message: String) : ApiException(
    code,
    message
)

class ParseException(message: String = "数据解析失败") : ApiException(
    ErrorCode.ERROR_PARSE,
    message
)
