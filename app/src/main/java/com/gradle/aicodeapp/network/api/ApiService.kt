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
     * 搜索文章
     * @param page 页码，从0开始
     * @param k 搜索关键词
     */
    @GET("article/query/{page}/json")
    suspend fun searchArticles(
        @Path("page") page: Int,
        @Query("k") keyword: String
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

    /**
     * 收藏站内文章
     * @param id 文章ID
     */
    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(
        @Path("id") id: Int
    ): ApiResponse<Any>

    /**
     * 收藏站外文章
     * @param title 文章标题
     * @param author 作者
     * @param link 文章链接
     */
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    suspend fun collectOutsideArticle(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): ApiResponse<Any>

    /**
     * 取消收藏（站内文章）
     * @param id 文章ID
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollectArticle(
        @Path("id") id: Int
    ): ApiResponse<Any>

    /**
     * 取消收藏（站外文章）
     * @param id 收藏ID
     */
    @POST("lg/uncollect/{id}/json")
    suspend fun uncollectOutsideArticle(
        @Path("id") id: Int
    ): ApiResponse<Any>

    /**
     * 获取收藏列表
     * @param page 页码，从0开始
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectList(
        @Path("page") page: Int
    ): ApiResponse<ArticleListResponse>

    /**
     * 编辑收藏文章
     * @param id 文章ID
     * @param title 文章标题
     * @param author 作者
     * @param link 文章链接
     */
    @FormUrlEncoded
    @POST("lg/collect/user_article/update/{id}/json")
    suspend fun updateCollectArticle(
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): ApiResponse<Any>

    // 可以添加更多API方法
}
