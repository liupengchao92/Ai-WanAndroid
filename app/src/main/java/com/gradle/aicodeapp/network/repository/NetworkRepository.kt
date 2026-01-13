package com.gradle.aicodeapp.network.repository

import com.gradle.aicodeapp.network.api.ApiService
import com.gradle.aicodeapp.network.model.ApiResponse
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.model.ArticleListResponse
import com.gradle.aicodeapp.network.model.Banner
import com.gradle.aicodeapp.network.model.Friend
import com.gradle.aicodeapp.network.model.LoginRequest
import com.gradle.aicodeapp.network.model.LoginResponse
import com.gradle.aicodeapp.network.model.RegisterRequest
import com.gradle.aicodeapp.network.model.RegisterResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val apiService: ApiService
) {

    /**
     * 获取首页文章列表
     * @param page 页码，从0开始
     */
    suspend fun getHomeArticles(page: Int): Result<ApiResponse<ArticleListResponse>> {
        return try {
            val response = apiService.getHomeArticles(page)
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 获取首页Banner
     */
    suspend fun getBanners(): Result<ApiResponse<List<Banner>>> {
        return try {
            val response = apiService.getBanners()
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 获取常用网站
     */
    suspend fun getFriends(): Result<ApiResponse<List<Friend>>> {
        return try {
            val response = apiService.getFriends()
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 获取搜索热词
     */
    suspend fun getHotKeys(): Result<ApiResponse<List<Friend>>> {
        return try {
            val response = apiService.getHotKeys()
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 获取置顶文章
     */
    suspend fun getTopArticles(): Result<ApiResponse<List<Article>>> {
        return try {
            val response = apiService.getTopArticles()
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 获取广场文章列表
     * @param page 页码，从0开始
     */
    suspend fun getSquareArticles(page: Int): Result<ApiResponse<ArticleListResponse>> {
        return try {
            val response = apiService.getSquareArticles(page)
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     */
    suspend fun login(username: String, password: String): Result<ApiResponse<LoginResponse>> {
        return try {
            val loginRequest = LoginRequest(username, password)
            val response = apiService.login(loginRequest)
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param repassword 确认密码
     */
    suspend fun register(
        username: String,
        password: String,
        repassword: String
    ): Result<ApiResponse<RegisterResponse>> {
        return try {
            val registerRequest = RegisterRequest(username, password, repassword)
            val response = apiService.register(registerRequest)
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 退出登录
     */
    suspend fun logout(): Result<ApiResponse<Any>> {
        return try {
            val response = apiService.logout()
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 处理API响应
     * @param response API响应
     * @param T 响应数据类型
     */
    private fun <T> handleApiResponse(response: ApiResponse<T>): Result<ApiResponse<T>> {
        return if (response.isSuccess()) {
            Result.success(response)
        } else {
            Result.failure(Exception(response.errorMsg))
        }
    }

    // 可以添加更多网络请求方法
}
