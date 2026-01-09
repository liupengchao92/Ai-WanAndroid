package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.SquareUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SquareViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(SquareUiState())
    val uiState: StateFlow<SquareUiState> = _uiState

    // 加载数据
    fun loadData() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        // 加载广场文章
        viewModelScope.launch {
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
    }

    // 下拉刷新
    fun refreshData() {
        _uiState.value = _uiState.value.copy(
            currentPage = 0,
            isRefreshing = true
        )
        loadData()
    }

    // 加载更多
    fun loadMore() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMore) {
            _uiState.value = _uiState.value.copy(
                currentPage = currentState.currentPage + 1
            )
            loadData()
        }
    }

    // 清除错误信息
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}