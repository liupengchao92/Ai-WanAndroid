package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.MessageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState

    private val TAG = "MessageViewModel"
    private val PAGE_SIZE = 20

    /**
     * 获取未读消息数量
     */
    fun loadUnreadCount() {
        viewModelScope.launch {
            val result = networkRepository.getUnreadMessageCount()
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    _uiState.value = _uiState.value.copy(
                        unreadCount = response.data ?: 0,
                        isLoginRequired = false
                    )
                } else {
                    if (response?.isLoginExpired() == true) {
                        _uiState.value = _uiState.value.copy(
                            isLoginRequired = true
                        )
                    }
                }
            }
        }
    }

    /**
     * 加载已读消息列表
     * @param page 页码，从1开始
     * @param isRefresh 是否刷新
     */
    fun loadReadMessages(page: Int = 1, isRefresh: Boolean = false) {
        if (isRefresh) {
            _uiState.value = _uiState.value.copy(
                isRefreshing = true,
                errorMessage = null
            )
        } else if (page == 1) {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
        } else {
            _uiState.value = _uiState.value.copy(
                isLoadingMore = true
            )
        }

        viewModelScope.launch {
            val result = networkRepository.getReadMessageList(page, PAGE_SIZE)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    val newMessages = response.data?.datas ?: emptyList()
                    val updatedMessages = if (page == 1 || isRefresh) {
                        newMessages
                    } else {
                        _uiState.value.readMessages + newMessages
                    }

                    _uiState.value = _uiState.value.copy(
                        readMessages = updatedMessages,
                        currentReadPage = page,
                        hasMoreReadData = response.data?.hasMore() ?: false,
                        isLoading = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                        isLoginRequired = false
                    )
                } else {
                    if (response?.isLoginExpired() == true) {
                        _uiState.value = _uiState.value.copy(
                            isLoginRequired = true,
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingMore = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "获取已读消息失败: ${response?.errorMsg}",
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingMore = false
                        )
                    }
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${result.exceptionOrNull()?.message}",
                    isLoading = false,
                    isRefreshing = false,
                    isLoadingMore = false
                )
            }
        }
    }

    /**
     * 加载未读消息列表
     * @param page 页码，从1开始
     * @param isRefresh 是否刷新
     */
    fun loadUnreadMessages(page: Int = 1, isRefresh: Boolean = false) {
        if (isRefresh) {
            _uiState.value = _uiState.value.copy(
                isRefreshing = true,
                errorMessage = null
            )
        } else if (page == 1) {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
        } else {
            _uiState.value = _uiState.value.copy(
                isLoadingMore = true
            )
        }

        viewModelScope.launch {
            val result = networkRepository.getUnreadMessageList(page, PAGE_SIZE)
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response?.isSuccess() == true) {
                    val newMessages = response.data?.datas ?: emptyList()
                    val updatedMessages = if (page == 1 || isRefresh) {
                        newMessages
                    } else {
                        _uiState.value.unreadMessages + newMessages
                    }

                    _uiState.value = _uiState.value.copy(
                        unreadMessages = updatedMessages,
                        currentUnreadPage = page,
                        hasMoreUnreadData = response.data?.hasMore() ?: false,
                        isLoading = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                        isLoginRequired = false,
                        unreadMessageLoaded = true,
                        unreadCount = 0
                    )
                } else {
                    if (response?.isLoginExpired() == true) {
                        _uiState.value = _uiState.value.copy(
                            isLoginRequired = true,
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingMore = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "获取未读消息失败: ${response?.errorMsg}",
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingMore = false
                        )
                    }
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "网络错误: ${result.exceptionOrNull()?.message}",
                    isLoading = false,
                    isRefreshing = false,
                    isLoadingMore = false
                )
            }
        }
    }

    /**
     * 加载更多已读消息
     */
    fun loadMoreReadMessages() {
        val currentState = _uiState.value
        if (!currentState.isLoadingMore && currentState.hasMoreReadData) {
            loadReadMessages(currentState.currentReadPage + 1)
        }
    }

    /**
     * 加载更多未读消息
     */
    fun loadMoreUnreadMessages() {
        val currentState = _uiState.value
        if (!currentState.isLoadingMore && currentState.hasMoreUnreadData) {
            loadUnreadMessages(currentState.currentUnreadPage + 1)
        }
    }

    /**
     * 刷新已读消息列表
     */
    fun refreshReadMessages() {
        loadReadMessages(1, isRefresh = true)
    }

    /**
     * 刷新未读消息列表
     */
    fun refreshUnreadMessages() {
        loadUnreadMessages(1, isRefresh = true)
    }

    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    /**
     * 重置未读消息加载状态
     */
    fun resetUnreadMessageLoaded() {
        _uiState.value = _uiState.value.copy(
            unreadMessageLoaded = false
        )
    }
}
