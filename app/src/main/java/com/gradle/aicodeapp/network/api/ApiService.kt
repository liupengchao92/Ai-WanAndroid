package com.gradle.aicodeapp.network.api

import com.gradle.aicodeapp.network.model.ApiResponse
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.model.ArticleListResponse
import com.gradle.aicodeapp.network.model.Banner
import com.gradle.aicodeapp.network.model.Friend
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /**
     * 首页文章列表
     * @param page 页码，从0开始
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeArticles(
        @Path("page") page: Int
    ): ApiResponse<ArticleListResponse>

    /**
     * 首页Banner
     */
    @GET("banner/json")
    suspend fun getBanners(): ApiResponse<List<Banner>>

    /**
     * 常用网站
     */
    @GET("friend/json")
    suspend fun getFriends(): ApiResponse<List<Friend>>

    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    suspend fun getHotKeys(): ApiResponse<List<Friend>>

    /**
     * 置顶文章
     */
    @GET("article/top/json")
    suspend fun getTopArticles(): ApiResponse<List<Article>>

    /**
     * 广场文章列表
     * @param page 页码，从0开始
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareArticles(
        @Path("page") page: Int
    ): ApiResponse<ArticleListResponse>

    // 可以添加更多API方法
}
