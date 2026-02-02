package com.gradle.aicodeapp.network.repository

import com.gradle.aicodeapp.network.api.ApiService
import com.gradle.aicodeapp.network.exception.ErrorHandler
import com.gradle.aicodeapp.network.model.ApiResponse
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.model.ArticleListResponse
import com.gradle.aicodeapp.network.model.Banner
import com.gradle.aicodeapp.network.model.CoinRecordResponse
import com.gradle.aicodeapp.network.model.CoinRankResponse
import com.gradle.aicodeapp.network.model.CoinUserInfo
import com.gradle.aicodeapp.network.model.Friend
import com.gradle.aicodeapp.network.model.LoginResponse
import com.gradle.aicodeapp.network.model.NavigationGroup
import com.gradle.aicodeapp.network.model.PopularColumn
import com.gradle.aicodeapp.network.model.PopularColumnResponse
import com.gradle.aicodeapp.network.model.PopularRoute
import com.gradle.aicodeapp.network.model.PopularRouteResponse
import com.gradle.aicodeapp.network.model.PopularWenda
import com.gradle.aicodeapp.network.model.PopularWendaResponse
import com.gradle.aicodeapp.network.model.ProjectCategory
import com.gradle.aicodeapp.network.model.RegisterResponse
import com.gradle.aicodeapp.network.model.Todo
import com.gradle.aicodeapp.network.model.TodoListResponse
import com.gradle.aicodeapp.network.model.WendaListResponse
import com.gradle.aicodeapp.network.model.WendaCommentResponse
import com.gradle.aicodeapp.network.model.WxOfficialAccountResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
        return safeApiCall { apiService.getHomeArticles(page) }
    }

    /**
     * 获取首页Banner
     */
    suspend fun getBanners(): Result<ApiResponse<List<Banner>>> {
        return safeApiCall { apiService.getBanners() }
    }

    /**
     * 获取常用网站
     */
    suspend fun getFriends(): Result<ApiResponse<List<Friend>>> {
        return safeApiCall { apiService.getFriends() }
    }

    /**
     * 获取搜索热词
     */
    suspend fun getHotKeys(): Result<ApiResponse<List<Friend>>> {
        return safeApiCall { apiService.getHotKeys() }
    }

    /**
     * 获取置顶文章
     */
    suspend fun getTopArticles(): Result<ApiResponse<List<Article>>> {
        return safeApiCall { apiService.getTopArticles() }
    }

    /**
     * 获取广场文章列表
     * @param page 页码，从0开始
     */
    suspend fun getSquareArticles(page: Int): Result<ApiResponse<ArticleListResponse>> {
        return safeApiCall { apiService.getSquareArticles(page) }
    }

    /**
     * 搜索文章
     * @param page 页码，从0开始
     * @param keyword 搜索关键词
     */
    suspend fun searchArticles(page: Int, keyword: String): Result<ApiResponse<ArticleListResponse>> {
        return safeApiCall { apiService.searchArticles(page, keyword) }
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     */
    suspend fun login(username: String, password: String): Result<ApiResponse<LoginResponse>> {
        return safeApiCall { apiService.login(username, password) }
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
        return safeApiCall { apiService.register(username, password, repassword) }
    }

    /**
     * 退出登录
     */
    suspend fun logout(): Result<ApiResponse<Any>> {
        return safeApiCall { apiService.logout() }
    }

    /**
     * 获取项目分类
     */
    suspend fun getProjectCategories(): Result<ApiResponse<List<ProjectCategory>>> {
        return safeApiCall { apiService.getProjectCategories() }
    }

    /**
     * 获取项目列表
     * @param page 页码，从1开始
     * @param cid 分类id
     */
    suspend fun getProjectList(page: Int, cid: Int): Result<ApiResponse<ArticleListResponse>> {
        return safeApiCall { apiService.getProjectList(page, cid) }
    }

    /**
     * 获取导航数据
     */
    suspend fun getNavigationData(): Result<ApiResponse<List<NavigationGroup>>> {
        return safeApiCall { apiService.getNavigationData() }
    }

    /**
     * 收藏站内文章
     * @param id 文章ID
     */
    suspend fun collectArticle(id: Int): Result<ApiResponse<Any>> {
        return safeApiCall { apiService.collectArticle(id) }
    }

    /**
     * 收藏站外文章
     * @param title 文章标题
     * @param author 作者
     * @param link 文章链接
     */
    suspend fun collectOutsideArticle(
        title: String,
        author: String,
        link: String
    ): Result<ApiResponse<Any>> {
        return safeApiCall { apiService.collectOutsideArticle(title, author, link) }
    }

    /**
     * 取消收藏（站内文章）
     * @param id 文章ID
     */
    suspend fun uncollectArticle(id: Int): Result<ApiResponse<Any>> {
        return safeApiCall { apiService.uncollectArticle(id) }
    }

    /**
     * 取消收藏（站外文章）
     * @param id 收藏ID
     */
    suspend fun uncollectOutsideArticle(id: Int): Result<ApiResponse<Any>> {
        return safeApiCall { apiService.uncollectOutsideArticle(id) }
    }

    /**
     * 获取收藏列表
     * @param page 页码，从0开始
     */
    suspend fun getCollectList(page: Int): Result<ApiResponse<ArticleListResponse>> {
        return safeApiCall { apiService.getCollectList(page) }
    }

    /**
     * 编辑收藏文章
     * @param id 文章ID
     * @param title 文章标题
     * @param author 作者
     * @param link 文章链接
     */
    suspend fun updateCollectArticle(
        id: Int,
        title: String,
        author: String,
        link: String
    ): Result<ApiResponse<Any>> {
        return safeApiCall { apiService.updateCollectArticle(id, title, author, link) }
    }

    suspend fun getPopularWenda(): Result<PopularWendaResponse> {
        return try {
            val response = apiService.getPopularWenda()
            Result.success(response)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
            Result.failure(e)
        }
    }

    suspend fun getWendaList(page: Int, pageSize: Int? = null): Result<WendaListResponse> {
        return try {
            val response = apiService.getWendaList(page, pageSize)
            Result.success(response)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
            Result.failure(e)
        }
    }

    suspend fun getPopularColumn(): Result<PopularColumnResponse> {
        return try {
            val response = apiService.getPopularColumn()
            Result.success(response)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
            Result.failure(e)
        }
    }

    suspend fun getPopularRoute(): Result<PopularRouteResponse> {
        return try {
            val response = apiService.getPopularRoute()
            Result.success(response)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)

            Result.failure(e)
        }
    }

    suspend fun getCoinRank(page: Int): Result<ApiResponse<CoinRankResponse>> {
        return safeApiCall { apiService.getCoinRank(page) }
    }

    suspend fun getCoinUserInfo(): Result<ApiResponse<CoinUserInfo>> {
        return safeApiCall { apiService.getCoinUserInfo() }
    }

    suspend fun getCoinRecordList(page: Int): Result<ApiResponse<CoinRecordResponse>> {
        return safeApiCall { apiService.getCoinRecordList(page) }
    }

    suspend fun addTodo(
        title: String,
        content: String,
        date: Long,
        type: Int,
        priority: Int
    ): Result<ApiResponse<Todo>> {
        // Convert milliseconds to "YYYY-MM-DD" format for the API
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(Date(date))
        return safeApiCall { apiService.addTodo(title, content, dateString, type, priority) }
    }

    suspend fun updateTodo(
        id: Int,
        title: String,
        content: String,
        date: Long,
        type: Int,
        priority: Int,
        status: Int
    ): Result<ApiResponse<Todo>> {
        // Convert milliseconds to "YYYY-MM-DD" format for the API
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(Date(date))
        return safeApiCall { apiService.updateTodo(id, title, content, dateString, type, priority, status) }
    }

    suspend fun deleteTodo(id: Int): Result<ApiResponse<Any>> {
        return safeApiCall { apiService.deleteTodo(id) }
    }

    suspend fun toggleTodoStatus(id: Int, status: Int): Result<ApiResponse<Any>> {
        return safeApiCall { apiService.toggleTodoStatus(id, status) }
    }

    suspend fun getTodoList(
        page: Int,
        status: Int? = null,
        type: Int? = null,
        priority: Int? = null,
        orderby: Int? = null
    ): Result<ApiResponse<TodoListResponse>> {
        return safeApiCall { apiService.getTodoList(page, status, type, priority, orderby) }
    }

    /**
     * 安全的API调用，统一处理异常
     * @param apiCall API调用函数
     * @return Result对象，成功时包含API响应，失败时包含异常
     */
    private suspend fun <T> safeApiCall(apiCall: suspend () -> ApiResponse<T>): Result<ApiResponse<T>> {
        return try {
            val response = apiCall()
            Result.success(response)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
            Result.failure(e)
        }
    }

    /**
     * 获取公众号列表
     */
    suspend fun getWxOfficialAccounts(): Result<WxOfficialAccountResponse> {
        return try {
            val response = apiService.getWxOfficialAccounts()
            Result.success(response)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
            Result.failure(e)
        }
    }

    /**
     * 获取公众号文章列表
     * @param id 公众号ID
     * @param page 页码，从1开始
     * @param pageSize 每页数量，1-40，不传使用默认值
     */
    suspend fun getWxArticles(
        id: Int,
        page: Int,
        pageSize: Int? = null
    ): Result<ApiResponse<ArticleListResponse>> {
        return safeApiCall { apiService.getWxArticles(id, page, pageSize) }
    }

    /**
     * 获取问答评论列表
     * @param wendaId 问答ID
     */
    suspend fun getWendaComments(wendaId: Int): Result<WendaCommentResponse> {
        return try {
            val response = apiService.getWendaComments(wendaId)
            Result.success(response)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
            Result.failure(e)
        }
    }

    // 可以添加更多网络请求方法
}
