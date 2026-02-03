package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

/**
 * 消息数据模型
 */
data class Message(
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("fromUser")
    val fromUser: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("isRead")
    val isRead: Int
) {
    /**
     * 判断消息是否已读
     */
    fun isReadMessage(): Boolean = isRead == 1
}

/**
 * 未读消息数量响应
 * 注意：API 返回的 data 直接是数字，不是对象
 */
data class UnreadMessageCountResponse(
    @SerializedName("data")
    val data: Int?,  // 直接是数字
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String
) {
    fun isSuccess(): Boolean = errorCode == 0
    fun isLoginExpired(): Boolean = errorCode == -1001
}

/**
 * 消息列表响应
 */
data class MessageListResponse(
    @SerializedName("data")
    val data: MessagePageData?,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String
) {
    fun isSuccess(): Boolean = errorCode == 0
    fun isLoginExpired(): Boolean = errorCode == -1001
}

/**
 * 消息分页数据
 */
data class MessagePageData(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Message>,
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
) {
    /**
     * 判断是否还有下一页
     */
    fun hasMore(): Boolean = !over
}
