package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.cache.CacheConfig
import com.gradle.aicodeapp.cache.CacheKeys
import com.gradle.aicodeapp.cache.DataCacheManager
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.CollectUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val cacheManager: DataCacheManager,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectUiState())
    val uiState: StateFlow<CollectUiState> = _uiState

    private val TAG = "CollectViewModel"

    fun loadCollectList() {
        if (!userManager.isLoggedIn()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "请先登录"
            )
            return
        }
        
        val currentState = _uiState.value
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            loadCollectListFromCacheOrNetwork()
        }
    }

    private suspend fun loadCollectListFromCacheOrNetwork() {
        val currentState = _uiState.value
        val cacheKey = CacheKeys.getCollectListKey(currentState.currentPage)
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
                hasMore = cachedArticles.size > 0
            )
            android.util.Log.d(TAG, "Collect list loaded from cache: ${cachedArticles.size}")
            return
        }

        val result = networkRepository.getCollectList(currentState.currentPage)
        if (result.isSuccess) {
            val response = result.getOrNull()
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
                android.util.Log.d(TAG, "Collect list loaded from network: ${newArticles.size}")
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "获取收藏列表失败: ${response?.errorMsg}",
                    isLoading = false,
                    isRefreshing = false
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "网络错误: ${result.exceptionOrNull()?.message}",
                isLoading = false,
                isRefreshing = false
            )
        }
    }

    fun collectArticle(articleId: Int) {
        if (!userManager.isLoggedIn()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "请先登录"
            )
            return
        }
        
        viewModelScope.launch {
            val result = networkRepository.collectArticle(articleId)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    android.util.Log.d(TAG, "Article collected successfully: $articleId")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "收藏失败: ${response?.errorMsg}"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun collectOutsideArticle(title: String, author: String, link: String) {
        viewModelScope.launch {
            val result = networkRepository.collectOutsideArticle(title, author, link)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    android.util.Log.d(TAG, "Outside article collected successfully")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "收藏失败: ${response?.errorMsg}"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun uncollectArticle(articleId: Int) {
        if (!userManager.isLoggedIn()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "请先登录"
            )
            return
        }
        
        viewModelScope.launch {
            val result = networkRepository.uncollectArticle(articleId)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    android.util.Log.d(TAG, "Article uncollected successfully: $articleId")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "取消收藏失败: ${response?.errorMsg}"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun uncollectOutsideArticle(collectId: Int) {
        viewModelScope.launch {
            val result = networkRepository.uncollectOutsideArticle(collectId)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    android.util.Log.d(TAG, "Outside article uncollected successfully: $collectId")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "取消收藏失败: ${response?.errorMsg}"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun updateCollectArticle(articleId: Int, title: String, author: String, link: String) {
        viewModelScope.launch {
            val result = networkRepository.updateCollectArticle(articleId, title, author, link)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    android.util.Log.d(TAG, "Collect article updated successfully: $articleId")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "更新失败: ${response?.errorMsg}"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            cacheManager.remove(CacheKeys.getCollectListKey(0))
            
            _uiState.value = _uiState.value.copy(
                currentPage = 0,
                isRefreshing = true,
                articles = emptyList()
            )
            
            loadCollectListFromCacheOrNetwork()
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMore) {
            _uiState.value = currentState.copy(
                currentPage = currentState.currentPage + 1
            )
            viewModelScope.launch {
                loadCollectListFromCacheOrNetwork()
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}
