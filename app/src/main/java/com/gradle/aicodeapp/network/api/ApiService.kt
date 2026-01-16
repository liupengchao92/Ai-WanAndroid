package com.gradle.aicodeapp.network.api

import com.gradle.aicodeapp.network.model.ApiResponse
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.model.ArticleListResponse
import com.gradle.aicodeapp.network.model.Banner
import com.gradle.aicodeapp.network.model.Friend
import com.gradle.aicodeapp.network.model.LoginResponse
import com.gradle.aicodeapp.network.model.NavigationGroup
import com.gradle.aicodeapp.network.model.ProjectCategory
import com.gradle.aicodeapp.network.model.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<LoginResponse>

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param repassword 确认密码
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): ApiResponse<RegisterResponse>

    /**
     * 退出登录
     */
    @GET("user/logout/json")
    suspend fun logout(): ApiResponse<Any>

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    suspend fun getProjectCategories(): ApiResponse<List<ProjectCategory>>

    /**
     * 项目列表
     * @param page 页码，从1开始
     * @param cid 分类id
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ArticleListResponse>

    /**
     * 导航数据
     */
    @GET("navi/json")
    suspend fun getNavigationData(): ApiResponse<List<NavigationGroup>>

    // 可以添加更多API方法
}
