package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

data class TodoListResponse(
    @SerializedName("datas")
    val datas: List<Todo> = emptyList(),
    @SerializedName("curPage")
    val curPage: Int = 0,
    @SerializedName("pageCount")
    val pageCount: Int = 0,
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("over")
    val over: Boolean = false
)
