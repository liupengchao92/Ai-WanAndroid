package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

data class WendaCommentResponse(
    @SerializedName("data")
    val data: WendaCommentData,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String
)

data class WendaCommentData(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<WendaComment>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
)

data class WendaComment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("anonymous")
    val anonymous: Int,
    @SerializedName("appendForContent")
    val appendForContent: Int,
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("canEdit")
    val canEdit: Boolean,
    @SerializedName("content")
    val content: String?,
    @SerializedName("niceDate")
    val niceDate: String?,
    @SerializedName("publishTime")
    val publishTime: Long,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("zan")
    val zan: Int,
    @SerializedName("tag")
    val tag: Int,
    @SerializedName("username")
    val username: String?,
    @SerializedName("replyComments")
    val replyComments: List<WendaReply>?
)

data class WendaReply(
    @SerializedName("id")
    val id: Int,
    @SerializedName("anonymous")
    val anonymous: Int,
    @SerializedName("articleId")
    val articleId: Int,
    @SerializedName("canEdit")
    val canEdit: Boolean,
    @SerializedName("content")
    val content: String?,
    @SerializedName("niceDate")
    val niceDate: String?,
    @SerializedName("publishTime")
    val publishTime: Long,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("zan")
    val zan: Int,
    @SerializedName("username")
    val username: String?,
    @SerializedName("toUserId")
    val toUserId: Int,
    @SerializedName("toUserName")
    val toUserName: String?
)
