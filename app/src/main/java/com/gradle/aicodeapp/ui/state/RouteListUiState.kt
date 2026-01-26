package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.PopularRoute

data class RouteListUiState(
    val routeList: List<PopularRoute> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val hasMore: Boolean = false
)