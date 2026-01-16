package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.ProjectUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState

    fun loadCategories() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val result = networkRepository.getProjectCategories()
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    val categories = response.data ?: emptyList()
                    val selectedCategory = categories.firstOrNull()
                    _uiState.value = _uiState.value.copy(
                        categories = categories,
                        isLoading = false
                    )
                    
                    if (selectedCategory != null) {
                        loadProjects(selectedCategory.id)
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "获取分类失败: ${response?.errorMsg}",
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

    fun loadProjects(categoryId: Int) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val result = networkRepository.getProjectList(currentState.currentPage, categoryId)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    val newProjects = response.data?.datas ?: emptyList()
                    val updatedProjects = if (currentState.currentPage == 1) {
                        newProjects
                    } else {
                        currentState.projects + newProjects
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        projects = updatedProjects,
                        isLoading = false,
                        isRefreshing = false,
                        hasMore = response.data?.over != true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "获取项目失败: ${response?.errorMsg}",
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
    }

    fun selectCategory(index: Int) {
        val currentState = _uiState.value
        if (index != currentState.selectedCategoryIndex && index in currentState.categories.indices) {
            _uiState.value = currentState.copy(
                selectedCategoryIndex = index,
                currentPage = 1,
                projects = emptyList()
            )
            loadProjects(currentState.categories[index].id)
        }
    }

    fun refreshData() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            currentPage = 1,
            isRefreshing = true
        )
        val selectedCategory = currentState.categories.getOrNull(currentState.selectedCategoryIndex)
        if (selectedCategory != null) {
            loadProjects(selectedCategory.id)
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (!currentState.isLoading && currentState.hasMore) {
            _uiState.value = currentState.copy(
                currentPage = currentState.currentPage + 1
            )
            val selectedCategory = currentState.categories.getOrNull(currentState.selectedCategoryIndex)
            if (selectedCategory != null) {
                loadProjects(selectedCategory.id)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}
