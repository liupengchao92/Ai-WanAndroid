package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

data class WendaListResponse(
    @SerializedName("data")
    val data: WendaListData,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String
)

data class WendaListData(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<PopularWenda>,
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