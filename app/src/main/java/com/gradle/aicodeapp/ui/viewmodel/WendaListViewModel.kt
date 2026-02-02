package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.network.model.PopularWenda
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.WendaListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WendaListViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WendaListUiState())
    val uiState: StateFlow<WendaListUiState> = _uiState

    private val TAG = "WendaListViewModel"

    fun loadData(pageSize: Int? = null) {
        val currentState = _uiState.value
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null,
            currentPage = 1,
            pageSize = pageSize
        )

        viewModelScope.launch {
            android.util.Log.d(TAG, "Starting to load wenda data from network... page=1, pageSize=$pageSize")
            val result = networkRepository.getWendaList(1, pageSize)
            if (result.isSuccess) {
                val response = result.getOrNull()
                android.util.Log.d(TAG, "API response received: errorCode=${response?.errorCode}, errorMsg=${response?.errorMsg}")
                if (response?.errorCode == 0) {
                    val data = response.data
                    val wendaList = data?.datas ?: emptyList()
                    android.util.Log.d(TAG, "Wenda data loaded successfully: ${wendaList.size} items, total=${data?.total}, pages=${data?.pageCount}")
                    _uiState.value = _uiState.value.copy(
                        wendaList = wendaList,
                        isLoading = false,
                        hasMore = !(data?.over ?: false),
                        currentPage = data?.curPage ?: 1,
                        totalPages = data?.pageCount ?: 0,
                        totalCount = data?.total ?: 0
                    )
                } else {
                    android.util.Log.e(TAG, "API error: ${response?.errorMsg}")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "加载失败: ${response?.errorMsg}",
                        isLoading = false
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                android.util.Log.e(TAG, "Network error: ${exception?.message}", exception)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${exception?.message}",
                    isLoading = false
                )
            }
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        
        if (currentState.isLoading || !currentState.hasMore) {
            android.util.Log.d(TAG, "Skip loadMore: isLoading=${currentState.isLoading}, hasMore=${currentState.hasMore}")
            return
        }

        val nextPage = currentState.currentPage + 1
        _uiState.value = currentState.copy(
            isLoading = true
        )

        viewModelScope.launch {
            android.util.Log.d(TAG, "Loading more wenda data... page=$nextPage, pageSize=${currentState.pageSize}")
            val result = networkRepository.getWendaList(nextPage, currentState.pageSize)
            if (result.isSuccess) {
                val response = result.getOrNull()
                android.util.Log.d(TAG, "Load more API response received: errorCode=${response?.errorCode}")
                if (response?.errorCode == 0) {
                    val data = response.data
                    val newWendaList = data?.datas ?: emptyList()
                    android.util.Log.d(TAG, "More wenda data loaded successfully: ${newWendaList.size} items")
                    _uiState.value = _uiState.value.copy(
                        wendaList = currentState.wendaList + newWendaList,
                        isLoading = false,
                        hasMore = !(data?.over ?: false),
                        currentPage = data?.curPage ?: nextPage,
                        totalPages = data?.pageCount ?: currentState.totalPages,
                        totalCount = data?.total ?: currentState.totalCount
                    )
                } else {
                    android.util.Log.e(TAG, "Load more API error: ${response?.errorMsg}")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "加载失败: ${response?.errorMsg}",
                        isLoading = false
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                android.util.Log.e(TAG, "Load more network error: ${exception?.message}", exception)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${exception?.message}",
                    isLoading = false
                )
            }
        }
    }

    fun refreshData() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            isRefreshing = true,
            errorMessage = null
        )

        viewModelScope.launch {
            android.util.Log.d(TAG, "Refreshing wenda data... page=1, pageSize=${currentState.pageSize}")
            val result = networkRepository.getWendaList(1, currentState.pageSize)
            if (result.isSuccess) {
                val response = result.getOrNull()
                android.util.Log.d(TAG, "Refresh API response received: errorCode=${response?.errorCode}")
                if (response?.errorCode == 0) {
                    val data = response.data
                    val wendaList = data?.datas ?: emptyList()
                    android.util.Log.d(TAG, "Wenda data refreshed successfully: ${wendaList.size} items")
                    _uiState.value = _uiState.value.copy(
                        wendaList = wendaList,
                        isRefreshing = false,
                        hasMore = !(data?.over ?: false),
                        currentPage = data?.curPage ?: 1,
                        totalPages = data?.pageCount ?: currentState.totalPages,
                        totalCount = data?.total ?: currentState.totalCount
                    )
                } else {
                    android.util.Log.e(TAG, "Refresh API error: ${response?.errorMsg}")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "加载失败: ${response?.errorMsg}",
                        isRefreshing = false
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                android.util.Log.e(TAG, "Refresh network error: ${exception?.message}", exception)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${exception?.message}",
                    isRefreshing = false
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    fun getWendaById(id: Int): PopularWenda? {
        return _uiState.value.wendaList.find { it.id == id }
    }
}