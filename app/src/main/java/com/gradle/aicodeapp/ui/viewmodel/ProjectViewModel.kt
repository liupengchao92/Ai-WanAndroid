package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.cache.CacheConfig
import com.gradle.aicodeapp.cache.CacheKeys
import com.gradle.aicodeapp.cache.DataCacheManager
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.ProjectUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val cacheManager: DataCacheManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState

    private val TAG = "ProjectViewModel"

    fun loadCategories() {
        val currentState = _uiState.value
        
        if (currentState.categories.isNotEmpty()) {
            return
        }
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            loadCategoriesFromCacheOrNetwork()
        }
    }

    private suspend fun loadCategoriesFromCacheOrNetwork() {
        val cacheKey = CacheKeys.PROJECT_CATEGORIES
        val expireTime = CacheConfig.getExpireTime(cacheKey)
        
        val cachedCategories = cacheManager.get<List<com.gradle.aicodeapp.network.model.ProjectCategory>>(cacheKey)
        if (cachedCategories != null) {
            val selectedCategory = cachedCategories.firstOrNull()
            _uiState.value = _uiState.value.copy(
                categories = cachedCategories,
                isLoading = false
            )
            android.util.Log.d(TAG, "Project categories loaded from cache: ${cachedCategories.size}")
            
            if (selectedCategory != null) {
                loadProjects(selectedCategory.id)
            }
            return
        }

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
                cacheManager.put(cacheKey, categories, expireTime)
                android.util.Log.d(TAG, "Project categories loaded from network: ${categories.size}")
                
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

    fun loadProjects(categoryId: Int) {
        val currentState = _uiState.value
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            loadProjectsFromCacheOrNetwork(categoryId)
        }
    }

    private suspend fun loadProjectsFromCacheOrNetwork(categoryId: Int) {
        val currentState = _uiState.value
        val cacheKey = CacheKeys.getProjectProjectsKey(categoryId, currentState.currentPage)
        val expireTime = CacheConfig.getExpireTime(cacheKey)
        
        val cachedProjects = cacheManager.get<List<Article>>(cacheKey)
        if (cachedProjects != null) {
            val updatedProjects = if (currentState.currentPage == 1) {
                cachedProjects
            } else {
                currentState.projects + cachedProjects
            }
            _uiState.value = _uiState.value.copy(
                projects = updatedProjects,
                isLoading = false,
                isRefreshing = false,
                hasMore = cachedProjects.size > 0
            )
            android.util.Log.d(TAG, "Projects loaded from cache: ${cachedProjects.size}")
            return
        }

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
                cacheManager.put(cacheKey, newProjects, expireTime)
                android.util.Log.d(TAG, "Projects loaded from network: ${newProjects.size}")
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
        viewModelScope.launch {
            val selectedCategory = currentState.categories.getOrNull(currentState.selectedCategoryIndex)
            if (selectedCategory != null) {
                cacheManager.remove(CacheKeys.getProjectProjectsKey(selectedCategory.id, 1))
            }
            
            _uiState.value = currentState.copy(
                currentPage = 1,
                isRefreshing = true,
                projects = emptyList()
            )
            
            val category = currentState.categories.getOrNull(currentState.selectedCategoryIndex)
            if (category != null) {
                loadProjectsFromCacheOrNetwork(category.id)
            }
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
                viewModelScope.launch {
                    loadProjectsFromCacheOrNetwork(selectedCategory.id)
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}
