package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

/**
 * 常用网站信息
 */
data class Friend(
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("visible")
    val visible: Int
)
