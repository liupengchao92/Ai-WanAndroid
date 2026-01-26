package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

data class TodoRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("date")
    val date: Long,
    @SerializedName("type")
    val type: Int,
    @SerializedName("priority")
    val priority: Int,
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("id")
    val id: Int? = null
)
