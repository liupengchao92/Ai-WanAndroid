package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

data class PopularRouteResponse(
    @SerializedName("data")
    val data: List<PopularRoute>,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String
)

data class PopularRoute(
    @SerializedName("articleList")
    val articleList: List<Article>,
    @SerializedName("author")
    val author: String,
    @SerializedName("children")
    val children: List<Any>,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lisense")
    val lisense: String,
    @SerializedName("lisenseLink")
    val lisenseLink: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("parentChapterId")
    val parentChapterId: Int,
    @SerializedName("type")
    val type: Int,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean,
    @SerializedName("visible")
    val visible: Int
)
