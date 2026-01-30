package com.gradle.aicodeapp.data.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchHistoryRepository(
    private val searchHistoryDao: SearchHistoryDao
) {
    companion object {
        const val MAX_HISTORY_COUNT = 10
    }

    suspend fun addSearchHistory(keyword: String) {
        withContext(Dispatchers.IO) {
            if (keyword.isBlank()) return@withContext

            // 先检查是否已存在相同关键词
            val existingHistory = searchHistoryDao.findByKeyword(keyword)
            if (existingHistory != null) {
                // 如果存在，更新时间戳
                val updatedHistory = existingHistory.copy(timestamp = System.currentTimeMillis())
                searchHistoryDao.insert(updatedHistory)
            } else {
                // 如果不存在，添加新记录
                val newHistory = SearchHistory(keyword = keyword)
                searchHistoryDao.insert(newHistory)
            }

            // 检查历史记录数量，超过限制则删除最旧的
            val historyCount = searchHistoryDao.count()
            if (historyCount > MAX_HISTORY_COUNT) {
                val allHistory = searchHistoryDao.getAll()
                val historiesToDelete = allHistory.takeLast(historyCount - MAX_HISTORY_COUNT)
                historiesToDelete.forEach {
                    searchHistoryDao.delete(it)
                }
            }
        }
    }

    suspend fun getSearchHistory(): List<SearchHistory> {
        return withContext(Dispatchers.IO) {
            searchHistoryDao.getAll()
        }
    }

    suspend fun deleteSearchHistory(id: Long) {
        withContext(Dispatchers.IO) {
            searchHistoryDao.deleteById(id)
        }
    }

    suspend fun clearSearchHistory() {
        withContext(Dispatchers.IO) {
            searchHistoryDao.deleteAll()
        }
    }
}