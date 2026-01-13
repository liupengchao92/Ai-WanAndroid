package com.gradle.aicodeapp.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户信息管理类
 * 用于管理用户的登录状态和用户信息
 */
@Singleton
class UserManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_NICKNAME = "nickname"
        private const val KEY_TOKEN = "token"
        private const val KEY_ICON = "icon"
    }

    /**
     * 保存用户登录信息
     */
    fun saveUserInfo(
        username: String,
        password: String,
        userId: Int?,
        nickname: String?,
        token: String?,
        icon: String?
    ) {
        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USERNAME, username)
            putString(KEY_PASSWORD, password)
            userId?.let { putInt(KEY_USER_ID, it) }
            putString(KEY_NICKNAME, nickname)
            putString(KEY_TOKEN, token)
            putString(KEY_ICON, icon)
            apply()
        }
    }

    /**
     * 清除用户登录信息
     */
    fun clearUserInfo() {
        sharedPreferences.edit().clear().apply()
    }

    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * 获取用户名
     */
    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    /**
     * 获取密码
     */
    fun getPassword(): String? {
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }

    /**
     * 获取用户ID
     */
    fun getUserId(): Int? {
        val userId = sharedPreferences.getInt(KEY_USER_ID, -1)
        return if (userId == -1) null else userId
    }

    /**
     * 获取昵称
     */
    fun getNickname(): String? {
        return sharedPreferences.getString(KEY_NICKNAME, null)
    }

    /**
     * 获取Token
     */
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    /**
     * 获取头像URL
     */
    fun getIcon(): String? {
        return sharedPreferences.getString(KEY_ICON, null)
    }

    /**
     * 获取登录凭据（用于自动登录）
     */
    fun getLoginCredentials(): Pair<String, String>? {
        val username = getUsername()
        val password = getPassword()
        return if (username != null && password != null) {
            Pair(username, password)
        } else {
            null
        }
    }
}
