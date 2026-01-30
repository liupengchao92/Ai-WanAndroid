package com.gradle.aicodeapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistory: SearchHistory)

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    suspend fun getAll(): List<SearchHistory>

    @Query("SELECT * FROM search_history WHERE keyword = :keyword LIMIT 1")
    suspend fun findByKeyword(keyword: String): SearchHistory?

    @Delete
    suspend fun delete(searchHistory: SearchHistory)

    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM search_history")
    suspend fun count(): Int

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecent(limit: Int): List<SearchHistory>
}