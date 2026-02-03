package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.Message

data class MessageUiState(
    val unreadCount: Int = 0,
    val readMessages: List<Message> = emptyList(),
    val unreadMessages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val currentReadPage: Int = 1,
    val hasMoreReadData: Boolean = true,
    val currentUnreadPage: Int = 1,
    val hasMoreUnreadData: Boolean = true,
    val isLoginRequired: Boolean = false,
    val unreadMessageLoaded: Boolean = false
)
