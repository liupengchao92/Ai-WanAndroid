package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.cache.CacheConfig
import com.gradle.aicodeapp.cache.CacheKeys
import com.gradle.aicodeapp.cache.DataCacheManager
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val cacheManager: DataCacheManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val TAG = "SearchViewModel"

    fun searchArticles(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                articles = emptyList(),
                isLoading = false,
                errorMessage = null,
                hasMore = false,
                currentQuery = query
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            currentQuery = query,
            currentPage = 0,
            isLoading = true,
            errorMessage = null,
            articles = emptyList()
        )

        viewModelScope.launch {
            performSearch(query, 0)
        }
    }

    private suspend fun performSearch(query: String, page: Int) {
        val cacheKey = CacheKeys.getSearchArticlesKey(query, page)
        val expireTime = CacheConfig.getExpireTime(cacheKey)
        
        val cachedArticles = cacheManager.get<List<Article>>(cacheKey)
        if (cachedArticles != null && page == 0) {
            _uiState.value = _uiState.value.copy(
                articles = cachedArticles,
                isLoading = false,
                hasMore = cachedArticles.isNotEmpty()
            )
            android.util.Log.d(TAG, "Search results loaded from cache: ${cachedArticles.size}")
            return
        }

        val searchResult = networkRepository.searchArticles(page, query)
        if (searchResult.isSuccess) {
            val response = searchResult.getOrNull()
            if (response?.isSuccess() == true) {
                val newArticles = response.data?.datas ?: emptyList()
                val updatedArticles = if (page == 0) {
                    newArticles
                } else {
                    _uiState.value.articles + newArticles
                }
                
                _uiState.value = _uiState.value.copy(
                    articles = updatedArticles,
                    isLoading = false,
                    hasMore = response.data?.over != true
                )
                
                if (newArticles.isNotEmpty()) {
                    cacheManager.put(cacheKey, newArticles, expireTime)
                }
                android.util.Log.d(TAG, "Search results loaded from network: ${newArticles.size}")
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "搜索失败: ${response?.errorMsg}",
                    isLoading = false
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "网络错误: ${searchResult.exceptionOrNull()?.message}",
                isLoading = false
            )
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMore && currentState.currentQuery.isNotBlank()) {
            _uiState.value = _uiState.value.copy(
                currentPage = currentState.currentPage + 1
            )
            viewModelScope.launch {
                performSearch(currentState.currentQuery, currentState.currentPage)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    fun clearResults() {
        _uiState.value = SearchUiState()
    }
}
