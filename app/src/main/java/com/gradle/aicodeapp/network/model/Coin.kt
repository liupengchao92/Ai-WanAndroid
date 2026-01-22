package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

data class CoinRank(
    @SerializedName("coinCount")
    val coinCount: Int,
    @SerializedName("level")
    val level: Int,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("rank")
    val rank: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("username")
    val username: String
)

data class CoinRankResponse(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<CoinRank>,
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

data class CoinUserInfo(
    @SerializedName("coinCount")
    val coinCount: Int,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("username")
    val username: String
)

data class CoinRecord(
    @SerializedName("coinCount")
    val coinCount: Int,
    @SerializedName("date")
    val date: Long,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userName")
    val userName: String
)

data class CoinRecordResponse(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<CoinRecord>,
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
