package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

data class PopularColumnResponse(
    @SerializedName("data")
    val data: List<PopularColumn>,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String
)

data class PopularColumn(
    @SerializedName("chapterId")
    val chapterId: Int,
    @SerializedName("chapterName")
    val chapterName: String,
    @SerializedName("columnId")
    val columnId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("subChapterId")
    val subChapterId: Int,
    @SerializedName("subChapterName")
    val subChapterName: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("userId")
    val userId: Int
)
