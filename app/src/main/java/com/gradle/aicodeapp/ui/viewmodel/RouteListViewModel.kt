package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.network.model.PopularRoute
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.RouteListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteListViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RouteListUiState())
    val uiState: StateFlow<RouteListUiState> = _uiState

    private val TAG = "RouteListViewModel"

    fun loadData() {
        val currentState = _uiState.value
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            android.util.Log.d(TAG, "Starting to load route data from network...")
            val result = networkRepository.getPopularRoute()
            if (result.isSuccess) {
                val response = result.getOrNull()
                android.util.Log.d(TAG, "API response received: errorCode=${response?.errorCode}, errorMsg=${response?.errorMsg}")
                if (response?.errorCode == 0) {
                    val data = response.data ?: emptyList()
                    android.util.Log.d(TAG, "Route data loaded successfully: ${data.size} items")
                    _uiState.value = _uiState.value.copy(
                        routeList = data,
                        isLoading = false,
                        hasMore = false
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

    fun refreshData() {
        loadData()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}