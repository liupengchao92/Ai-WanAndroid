package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.PopularColumn

data class ColumnListUiState(
    val columnList: List<PopularColumn> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val hasMore: Boolean = false
)