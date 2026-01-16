package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.model.ProjectCategory

data class ProjectUiState(
    val categories: List<ProjectCategory> = emptyList(),
    val projects: List<Article> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true
)
