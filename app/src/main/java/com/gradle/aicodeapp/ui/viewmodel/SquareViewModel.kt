package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.cache.CacheConfig
import com.gradle.aicodeapp.cache.CacheKeys
import com.gradle.aicodeapp.cache.DataCacheManager
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.SquareUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SquareViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val cacheManager: DataCacheManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SquareUiState())
    val uiState: StateFlow<SquareUiState> = _uiState

    private val TAG = "SquareViewModel"

    fun loadData() {
        val currentState = _uiState.value
        
        if (currentState.articles.isNotEmpty()) {
            return
        }
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            loadSquareArticles()
        }
    }

    private suspend fun loadSquareArticles() {
        val currentState = _uiState.value
        val cacheKey = CacheKeys.getSquareArticlesKey(currentState.currentPage)
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
            android.util.Log.d(TAG, "Square articles loaded from cache: ${cachedArticles.size}")
            return
        }

        val articleResult = networkRepository.getSquareArticles(currentState.currentPage)
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
                android.util.Log.d(TAG, "Square articles loaded from network: ${newArticles.size}")
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
            cacheManager.remove(CacheKeys.getSquareArticlesKey(0))
            
            _uiState.value = _uiState.value.copy(
                currentPage = 0,
                isRefreshing = true,
                articles = emptyList()
            )
            
            loadSquareArticles()
        }
    }

    fun refreshArticles() {
        viewModelScope.launch {
            cacheManager.remove(CacheKeys.getSquareArticlesKey(0))
            
            _uiState.value = _uiState.value.copy(
                currentPage = 0,
                isRefreshing = true,
                articles = emptyList()
            )
            
            loadSquareArticles()
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMore) {
            _uiState.value = _uiState.value.copy(
                currentPage = currentState.currentPage + 1
            )
            viewModelScope.launch {
                loadSquareArticles()
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}