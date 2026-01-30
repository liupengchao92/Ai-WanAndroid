package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.cache.CacheConfig
import com.gradle.aicodeapp.cache.CacheKeys
import com.gradle.aicodeapp.cache.DataCacheManager
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.WxArticleListUiState
import com.gradle.aicodeapp.utils.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WxArticleListViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val cacheManager: DataCacheManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WxArticleListUiState())
    val uiState: StateFlow<WxArticleListUiState> = _uiState

    private val TAG = "WxArticleListViewModel"

    /**
     * 初始化数据
     * @param accountId 公众号ID
     * @param accountName 公众号名称
     */
    fun initData(accountId: Int, accountName: String) {
        _uiState.value = WxArticleListUiState(
            accountId = accountId,
            accountName = accountName,
            isLoading = true
        )
        loadData()
    }

    /**
     * 加载数据
     */
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
            loadWxArticles()
        }
    }

    /**
     * 加载公众号文章列表
     */
    private suspend fun loadWxArticles() {
        val currentState = _uiState.value
        val cacheKey = CacheKeys.getWxArticlesKey(currentState.accountId, currentState.currentPage)
        val expireTime = CacheConfig.getExpireTime(cacheKey)

        // 尝试从缓存获取
        val cachedArticles = cacheManager.get<List<Article>>(cacheKey)
        if (cachedArticles != null) {
            val updatedArticles = if (currentState.currentPage == 1) {
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
            LogUtils.d(TAG, "Wx articles loaded from cache: ${cachedArticles.size}")
            return
        }

        // 从网络加载
        val result = networkRepository.getWxArticles(
            id = currentState.accountId,
            page = currentState.currentPage
        )

        if (result.isSuccess) {
            val response = result.getOrNull()
            if (response?.isSuccess() == true) {
                val newArticles = response.data?.datas ?: emptyList()
                val updatedArticles = if (currentState.currentPage == 1) {
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
                LogUtils.d(TAG, "Wx articles loaded from network: ${newArticles.size}")
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "获取文章失败: ${response?.errorMsg}",
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

    /**
     * 刷新数据
     */
    fun refreshData() {
        viewModelScope.launch {
            val accountId = _uiState.value.accountId
            // 清除第一页缓存
            cacheManager.remove(CacheKeys.getWxArticlesKey(accountId, 1))

            _uiState.value = _uiState.value.copy(
                currentPage = 1,
                isRefreshing = true,
                isLoading = true,
                articles = emptyList(),
                hasMore = true
            )

            loadWxArticles()
        }
    }

    /**
     * 加载更多
     */
    fun loadMore() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMore) {
            _uiState.value = _uiState.value.copy(
                currentPage = currentState.currentPage + 1
            )
            viewModelScope.launch {
                loadWxArticles()
            }
        }
    }

    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    /**
     * 更新文章收藏状态
     */
    fun updateArticleCollectStatus(articleId: Int, isCollected: Boolean) {
        val currentState = _uiState.value

        val updatedArticles = currentState.articles.map { article ->
            if (article.id == articleId) {
                article.copy(collect = isCollected)
            } else {
                article
            }
        }

        _uiState.value = currentState.copy(
            articles = updatedArticles
        )

        LogUtils.d(TAG, "Article collect status updated: articleId=$articleId, isCollected=$isCollected")
    }
}