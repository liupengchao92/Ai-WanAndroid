package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.PopularWenda

data class WendaListUiState(
    val wendaList: List<PopularWenda> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val hasMore: Boolean = false,
    val currentPage: Int = 1,
    val pageSize: Int? = null,
    val totalPages: Int = 0,
    val totalCount: Int = 0
)