package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.NavigationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NavigationUiState())
    val uiState: StateFlow<NavigationUiState> = _uiState

    private var hasLoadedData = false

    fun loadNavigationData() {
        if (hasLoadedData && _uiState.value.navigationGroups.isNotEmpty()) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val result = networkRepository.getNavigationData()
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    _uiState.value = _uiState.value.copy(
                        navigationGroups = response.data ?: emptyList(),
                        isLoading = false
                    )
                    hasLoadedData = true
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "获取导航数据失败: ${response?.errorMsg}",
                        isLoading = false
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${result.exceptionOrNull()?.message}",
                    isLoading = false
                )
            }
        }
    }

    fun selectGroup(index: Int) {
        val currentState = _uiState.value
        if (index != currentState.selectedGroupIndex && index in currentState.navigationGroups.indices) {
            _uiState.value = currentState.copy(
                selectedGroupIndex = index
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}
