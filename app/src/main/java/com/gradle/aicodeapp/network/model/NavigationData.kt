package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

data class NavigationGroup(
    @SerializedName("cid")
    val cid: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("articles")
    val articles: List<NavigationArticle>
)

data class NavigationArticle(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("niceDate")
    val niceDate: String,
    @SerializedName("envelopePic")
    val envelopePic: String
)
