package com.gradle.aicodeapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val keyword: String,
    val timestamp: Long = System.currentTimeMillis()
)