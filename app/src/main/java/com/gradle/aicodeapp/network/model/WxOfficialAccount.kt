package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

/**
 * 公众号数据模型
 */
data class WxOfficialAccount(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("parentChapterId")
    val parentChapterId: Int,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("author")
    val author: String = "",
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean = false,
    @SerializedName("visible")
    val visible: Int = 1,
    @SerializedName("children")
    val children: List<WxOfficialAccount> = emptyList(),
    @SerializedName("articleList")
    val articleList: List<Article> = emptyList()
)

/**
 * 公众号列表响应
 */
data class WxOfficialAccountResponse(
    @SerializedName("data")
    val data: List<WxOfficialAccount>?,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String
) {
    fun isSuccess(): Boolean = errorCode == 0
}