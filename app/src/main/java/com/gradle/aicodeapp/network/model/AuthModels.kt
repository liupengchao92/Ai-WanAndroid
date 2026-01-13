package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

/**
 * 登录响应数据
 */
data class LoginResponse(
    @SerializedName("admin")
    val admin: Boolean?,
    @SerializedName("chapterTops")
    val chapterTops: List<Any>?,
    @SerializedName("collectIds")
    val collectIds: List<Int>?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("publicName")
    val publicName: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("type")
    val type: Int?,
    @SerializedName("username")
    val username: String?
)

/**
 * 注册响应数据
 */
data class RegisterResponse(
    @SerializedName("admin")
    val admin: Boolean?,
    @SerializedName("chapterTops")
    val chapterTops: List<Any>?,
    @SerializedName("collectIds")
    val collectIds: List<Int>?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("publicName")
    val publicName: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("type")
    val type: Int?,
    @SerializedName("username")
    val username: String?
)

/**
 * 登录请求参数
 */
data class LoginRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)

/**
 * 注册请求参数
 */
data class RegisterRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("repassword")
    val repassword: String
)
