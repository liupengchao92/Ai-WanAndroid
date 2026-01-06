package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

/**
 * 通用API响应类，符合wanandroid API返回结构
 * @param T data字段的类型
 */
data class ApiResponse<T>(
    @SerializedName("data")
    val data: T?,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String
) {
    /**
     * 判断请求是否成功
     * errorCode == 0 代表执行成功
     */
    fun isSuccess(): Boolean = errorCode == 0

    /**
     * 判断登录是否失效
     * errorCode == -1001 代表登录失效
     */
    fun isLoginExpired(): Boolean = errorCode == -1001
}
