package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.Article

data class CollectUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val currentPage: Int = 0,
    val hasMore: Boolean = true
)
