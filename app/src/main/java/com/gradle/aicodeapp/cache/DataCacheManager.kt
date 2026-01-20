package com.gradle.aicodeapp.cache

import android.util.Log
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

data class CacheEntry<T>(
    val data: T,
    val timestamp: Long,
    val expireTime: Long
) {
    fun isExpired(): Boolean {
        return System.currentTimeMillis() - timestamp > expireTime
    }
}

class DataCacheManager {
    private val cacheMap = ConcurrentHashMap<String, CacheEntry<*>>()
    private val mutex = Mutex()
    
    private val TAG = "DataCacheManager"
    
    suspend fun <T> get(key: String): T? {
        return mutex.withLock {
            val entry = cacheMap[key] as? CacheEntry<T>
            if (entry != null) {
                if (entry.isExpired()) {
                    Log.d(TAG, "Cache expired for key: $key")
                    cacheMap.remove(key)
                    return null
                }
                Log.d(TAG, "Cache hit for key: $key")
                return entry.data
            }
            Log.d(TAG, "Cache miss for key: $key")
            return null
        }
    }
    
    suspend fun <T> put(key: String, data: T, expireTime: Long = 5 * 60 * 1000) {
        mutex.withLock {
            val entry = CacheEntry(
                data = data,
                timestamp = System.currentTimeMillis(),
                expireTime = expireTime
            )
            cacheMap[key] = entry
            Log.d(TAG, "Cache stored for key: $key, expireTime: ${expireTime}ms")
        }
    }
    
    suspend fun remove(key: String) {
        mutex.withLock {
            cacheMap.remove(key)
            Log.d(TAG, "Cache removed for key: $key")
        }
    }
    
    suspend fun clear() {
        mutex.withLock {
            cacheMap.clear()
            Log.d(TAG, "All cache cleared")
        }
    }
    
    suspend fun clearExpired() {
        mutex.withLock {
            val expiredKeys = cacheMap.filter { (_, entry) -> entry.isExpired() }.keys
            expiredKeys.forEach { key ->
                cacheMap.remove(key)
                Log.d(TAG, "Expired cache removed for key: $key")
            }
        }
    }
    
    suspend fun <T> getOrPut(
        key: String,
        expireTime: Long = 5 * 60 * 1000,
        loader: suspend () -> T
    ): T {
        val cached = get<T>(key)
        if (cached != null) {
            return cached
        }
        
        val data = loader()
        put(key, data, expireTime)
        return data
    }
    
    suspend fun exists(key: String): Boolean {
        val entry = cacheMap[key]
        if (entry != null) {
            if (entry.isExpired()) {
                remove(key)
                return false
            }
            return true
        }
        return false
    }
    
    suspend fun size(): Int {
        return mutex.withLock {
            cacheMap.size
        }
    }
    
    suspend fun keys(): Set<String> {
        return mutex.withLock {
            cacheMap.keys.toSet()
        }
    }
}

object CacheKeys {
    const val HOME_BANNERS = "home_banners"
    const val HOME_TOP_ARTICLES = "home_top_articles"
    const val HOME_ARTICLES = "home_articles"
    const val SQUARE_ARTICLES = "square_articles"
    const val PROJECT_CATEGORIES = "project_categories"
    const val PROJECT_PROJECTS = "project_projects"
    const val NAVIGATION_DATA = "navigation_data"
    const val COLLECT_LIST = "collect_list"
    
    fun getHomeArticlesKey(page: Int): String {
        return "${HOME_ARTICLES}_$page"
    }
    
    fun getSquareArticlesKey(page: Int): String {
        return "${SQUARE_ARTICLES}_$page"
    }
    
    fun getProjectProjectsKey(categoryId: Int, page: Int): String {
        return "${PROJECT_PROJECTS}_${categoryId}_$page"
    }
    
    fun getCollectListKey(page: Int): String {
        return "${COLLECT_LIST}_$page"
    }
}

object CacheConfig {
    const val DEFAULT_EXPIRE_TIME = 5 * 60 * 1000L
    const val SHORT_EXPIRE_TIME = 2 * 60 * 1000L
    const val LONG_EXPIRE_TIME = 10 * 60 * 1000L
    
    fun getExpireTime(key: String): Long {
        return when {
            key.contains("banners") -> LONG_EXPIRE_TIME
            key.contains("categories") -> LONG_EXPIRE_TIME
            key.contains("navigation") -> LONG_EXPIRE_TIME
            key.contains("articles") -> SHORT_EXPIRE_TIME
            key.contains("projects") -> SHORT_EXPIRE_TIME
            else -> DEFAULT_EXPIRE_TIME
        }
    }
}
