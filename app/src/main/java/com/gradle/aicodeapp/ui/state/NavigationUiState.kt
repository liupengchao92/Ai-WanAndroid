package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.NavigationGroup

data class NavigationUiState(
    val navigationGroups: List<NavigationGroup> = emptyList(),
    val selectedGroupIndex: Int = 0,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
