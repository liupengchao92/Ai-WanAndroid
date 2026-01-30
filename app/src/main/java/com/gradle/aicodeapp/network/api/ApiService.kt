package com.gradle.aicodeapp.network.api

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
import com.gradle.aicodeapp.network.model.WxOfficialAccountResponse
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
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun searchArticles(
        @Path("page") page: Int,
        @Field("k") keyword: String
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

    @GET("popular/wenda/json")
    suspend fun getPopularWenda(): PopularWendaResponse

    @GET("wenda/list/{page}/json")
    suspend fun getWendaList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int? = null
    ): WendaListResponse

    @GET("popular/column/json")
    suspend fun getPopularColumn(): PopularColumnResponse

    @GET("popular/route/json")
    suspend fun getPopularRoute(): PopularRouteResponse

    @GET("coin/rank/{page}/json")
    suspend fun getCoinRank(
        @Path("page") page: Int
    ): ApiResponse<CoinRankResponse>

    @GET("lg/coin/userinfo/json")
    suspend fun getCoinUserInfo(): ApiResponse<CoinUserInfo>

    @GET("lg/coin/list/{page}/json")
    suspend fun getCoinRecordList(
        @Path("page") page: Int
    ): ApiResponse<CoinRecordResponse>

    @FormUrlEncoded
    @POST("lg/todo/add/json")
    suspend fun addTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int
    ): ApiResponse<Todo>

    @FormUrlEncoded
    @POST("lg/todo/update/{id}/json")
    suspend fun updateTodo(
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int,
        @Field("status") status: Int
    ): ApiResponse<Todo>

    @POST("lg/todo/delete/{id}/json")
    suspend fun deleteTodo(
        @Path("id") id: Int
    ): ApiResponse<Any>

    @FormUrlEncoded
    @POST("lg/todo/done/{id}/json")
    suspend fun toggleTodoStatus(
        @Path("id") id: Int,
        @Field("status") status: Int
    ): ApiResponse<Any>

    @GET("lg/todo/v2/list/{page}/json")
    suspend fun getTodoList(
        @Path("page") page: Int,
        @Query("status") status: Int? = null,
        @Query("type") type: Int? = null,
        @Query("priority") priority: Int? = null,
        @Query("orderby") orderby: Int? = null
    ): ApiResponse<TodoListResponse>

    /**
     * 获取公众号列表
     */
    @GET("wxarticle/chapters/json")
    suspend fun getWxOfficialAccounts(): WxOfficialAccountResponse

    /**
     * 获取公众号文章列表
     * @param id 公众号ID
     * @param page 页码，从1开始
     * @param pageSize 每页数量，1-40，不传使用默认值
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWxArticles(
        @Path("id") id: Int,
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int? = null
    ): ApiResponse<ArticleListResponse>

    // 可以添加更多API方法
}
