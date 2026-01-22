package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.cache.CacheConfig
import com.gradle.aicodeapp.cache.CacheKeys
import com.gradle.aicodeapp.cache.DataCacheManager
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.model.Banner
import com.gradle.aicodeapp.network.model.PopularColumn
import com.gradle.aicodeapp.network.model.PopularRoute
import com.gradle.aicodeapp.network.model.PopularWenda
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val cacheManager: DataCacheManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val TAG = "HomeViewModel"

    fun loadData() {
        val currentState = _uiState.value
        
        if (currentState.banners.isNotEmpty() && currentState.topArticles.isNotEmpty() && currentState.articles.isNotEmpty()) {
            return
        }
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            loadBanners()
        }

        viewModelScope.launch {
            loadTopArticles()
        }

        viewModelScope.launch {
            loadHomeArticles()
        }
    }

    private suspend fun loadBanners() {
        val cacheKey = CacheKeys.HOME_BANNERS
        val expireTime = CacheConfig.getExpireTime(cacheKey)
        
        val cachedBanners = cacheManager.get<List<Banner>>(cacheKey)
        if (cachedBanners != null) {
            _uiState.value = _uiState.value.copy(banners = cachedBanners)
            android.util.Log.d(TAG, "Banners loaded from cache: ${cachedBanners.size}")
            return
        }

        val bannerResult = networkRepository.getBanners()
        if (bannerResult.isSuccess) {
            val response = bannerResult.getOrNull()
            if (response?.isSuccess() == true) {
                val banners = response.data ?: emptyList()
                _uiState.value = _uiState.value.copy(banners = banners)
                cacheManager.put(cacheKey, banners, expireTime)
                android.util.Log.d(TAG, "Banners loaded from network: ${banners.size}")
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "获取Banner失败: ${response?.errorMsg}"
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "网络错误: ${bannerResult.exceptionOrNull()?.message}"
            )
        }
    }

    private suspend fun loadTopArticles() {
        val cacheKey = CacheKeys.HOME_TOP_ARTICLES
        val expireTime = CacheConfig.getExpireTime(cacheKey)
        
        val cachedArticles = cacheManager.get<List<Article>>(cacheKey)
        if (cachedArticles != null) {
            _uiState.value = _uiState.value.copy(topArticles = cachedArticles)
            android.util.Log.d(TAG, "Top articles loaded from cache: ${cachedArticles.size}")
            return
        }

        val topResult = networkRepository.getTopArticles()
        if (topResult.isSuccess) {
            val response = topResult.getOrNull()
            if (response?.isSuccess() == true) {
                val articles = response.data ?: emptyList()
                _uiState.value = _uiState.value.copy(topArticles = articles)
                cacheManager.put(cacheKey, articles, expireTime)
                android.util.Log.d(TAG, "Top articles loaded from network: ${articles.size}")
            }
        }
    }

    private suspend fun loadHomeArticles() {
        val currentState = _uiState.value
        val cacheKey = CacheKeys.getHomeArticlesKey(currentState.currentPage)
        val expireTime = CacheConfig.getExpireTime(cacheKey)
        
        val cachedArticles = cacheManager.get<List<Article>>(cacheKey)
        if (cachedArticles != null) {
            val updatedArticles = if (currentState.currentPage == 0) {
                cachedArticles
            } else {
                currentState.articles + cachedArticles
            }
            _uiState.value = _uiState.value.copy(
                articles = updatedArticles,
                isLoading = false,
                isRefreshing = false,
                hasMore = cachedArticles.isNotEmpty()
            )
            android.util.Log.d(TAG, "Home articles loaded from cache: ${cachedArticles.size}")
            return
        }

        val articleResult = networkRepository.getHomeArticles(currentState.currentPage)
        if (articleResult.isSuccess) {
            val response = articleResult.getOrNull()
            if (response?.isSuccess() == true) {
                val newArticles = response.data?.datas ?: emptyList()
                val updatedArticles = if (currentState.currentPage == 0) {
                    newArticles
                } else {
                    currentState.articles + newArticles
                }
                
                _uiState.value = _uiState.value.copy(
                    articles = updatedArticles,
                    isLoading = false,
                    isRefreshing = false,
                    hasMore = response.data?.over != true
                )
                cacheManager.put(cacheKey, newArticles, expireTime)
                android.util.Log.d(TAG, "Home articles loaded from network: ${newArticles.size}")
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "获取文章失败: ${response?.errorMsg}",
                    isLoading = false,
                    isRefreshing = false
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "网络错误: ${articleResult.exceptionOrNull()?.message}",
                isLoading = false,
                isRefreshing = false
            )
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            cacheManager.remove(CacheKeys.HOME_BANNERS)
            cacheManager.remove(CacheKeys.HOME_TOP_ARTICLES)
            cacheManager.remove(CacheKeys.getHomeArticlesKey(0))
            
            _uiState.value = _uiState.value.copy(
                currentPage = 0,
                isLoading = true,
                isRefreshing = true,
                banners = emptyList(),
                topArticles = emptyList(),
                articles = emptyList()
            )
            
            loadBanners()
            loadTopArticles()
            loadHomeArticles()
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            cacheManager.remove(CacheKeys.HOME_TOP_ARTICLES)
            cacheManager.remove(CacheKeys.getHomeArticlesKey(0))
            
            _uiState.value = _uiState.value.copy(
                currentPage = 0,
                isLoading = true,
                isRefreshing = true,
                topArticles = emptyList(),
                articles = emptyList()
            )
            
            loadTopArticles()
            loadHomeArticles()
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMore) {
            _uiState.value = _uiState.value.copy(
                currentPage = currentState.currentPage + 1
            )
            viewModelScope.launch {
                loadHomeArticles()
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    fun updateArticleCollectStatus(articleId: Int, isCollected: Boolean) {
        val currentState = _uiState.value
        
        val updatedTopArticles = currentState.topArticles.map { article ->
            if (article.id == articleId) {
                article.copy(collect = isCollected)
            } else {
                article
            }
        }
        
        val updatedArticles = currentState.articles.map { article ->
            if (article.id == articleId) {
                article.copy(collect = isCollected)
            } else {
                article
            }
        }
        
        _uiState.value = currentState.copy(
            topArticles = updatedTopArticles,
            articles = updatedArticles
        )
        
        android.util.Log.d(TAG, "Article collect status updated: articleId=$articleId, isCollected=$isCollected")
    }

    suspend fun getPopularWenda(): List<PopularWenda> {
        val cacheKey = CacheKeys.POPULAR_WENDA
        val expireTime = CacheConfig.getExpireTime(cacheKey)
        
        val cachedData = cacheManager.get<List<PopularWenda>>(cacheKey)
        if (cachedData != null) {
            android.util.Log.d(TAG, "Popular wenda loaded from cache: ${cachedData.size}")
            return cachedData
        }

        val result = networkRepository.getPopularWenda()
        if (result.isSuccess) {
            val response = result.getOrNull()
            if (response?.errorCode == 0) {
                val data = response.data ?: emptyList()
                cacheManager.put(cacheKey, data, expireTime)
                android.util.Log.d(TAG, "Popular wenda loaded from network: ${data.size}")
                return data
            }
        }
        return emptyList()
    }

    suspend fun getPopularColumn(): List<PopularColumn> {
        val cacheKey = CacheKeys.POPULAR_COLUMN
        val expireTime = CacheConfig.getExpireTime(cacheKey)
        
        val cachedData = cacheManager.get<List<PopularColumn>>(cacheKey)
        if (cachedData != null) {
            android.util.Log.d(TAG, "Popular column loaded from cache: ${cachedData.size}")
            return cachedData
        }

        val result = networkRepository.getPopularColumn()
        if (result.isSuccess) {
            val response = result.getOrNull()
            if (response?.errorCode == 0) {
                val data = response.data ?: emptyList()
                cacheManager.put(cacheKey, data, expireTime)
                android.util.Log.d(TAG, "Popular column loaded from network: ${data.size}")
                return data
            }
        }
        return emptyList()
    }

    suspend fun getPopularRoute(): List<PopularRoute> {
        val cacheKey = CacheKeys.POPULAR_ROUTE
        val expireTime = CacheConfig.getExpireTime(cacheKey)
        
        val cachedData = cacheManager.get<List<PopularRoute>>(cacheKey)
        if (cachedData != null) {
            android.util.Log.d(TAG, "Popular route loaded from cache: ${cachedData.size}")
            return cachedData
        }

        val result = networkRepository.getPopularRoute()
        if (result.isSuccess) {
            val response = result.getOrNull()
            if (response?.errorCode == 0) {
                val data = response.data ?: emptyList()
                cacheManager.put(cacheKey, data, expireTime)
                android.util.Log.d(TAG, "Popular route loaded from network: ${data.size}")
                return data
            }
        }
        return emptyList()
    }
}
