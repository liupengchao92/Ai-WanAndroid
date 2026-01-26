package com.gradle.aicodeapp.network.model

import com.google.gson.annotations.SerializedName

data class Todo(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("content")
    val content: String = "",
    @SerializedName("date")
    val date: Long = 0L,
    @SerializedName("type")
    val type: Int = 1,
    @SerializedName("priority")
    val priority: Int = 1,
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("completeDate")
    val completeDate: Long? = null,
    @SerializedName("userId")
    val userId: Int = 0
) {
    companion object {
        const val TYPE_WORK = 1
        const val TYPE_LIFE = 2
        const val TYPE_ENTERTAINMENT = 3

        const val PRIORITY_HIGH = 1
        const val PRIORITY_NORMAL = 2

        const val STATUS_INCOMPLETE = 0
        const val STATUS_COMPLETED = 1
    }

    fun getTypeName(): String {
        return when (type) {
            TYPE_WORK -> "工作"
            TYPE_LIFE -> "生活"
            TYPE_ENTERTAINMENT -> "娱乐"
            else -> "其他"
        }
    }

    fun getPriorityName(): String {
        return when (priority) {
            PRIORITY_HIGH -> "高"
            PRIORITY_NORMAL -> "普通"
            else -> "普通"
        }
    }

    fun isCompleted(): Boolean = status == STATUS_COMPLETED
}
