package com.gradle.aicodeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.network.model.PopularWenda
import com.gradle.aicodeapp.network.model.WendaComment
import com.gradle.aicodeapp.network.repository.NetworkRepository
import com.gradle.aicodeapp.ui.state.WendaDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WendaDetailViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WendaDetailUiState())
    val uiState: StateFlow<WendaDetailUiState> = _uiState

    private val TAG = "WendaDetailViewModel"

    private var currentWendaId: Int = -1

    fun setWenda(wenda: PopularWenda) {
        _uiState.value = _uiState.value.copy(wenda = wenda)
        currentWendaId = wenda.id
        loadComments(wenda.id)
    }

    fun loadComments(wendaId: Int) {
        if (wendaId <= 0) return

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                val result = networkRepository.getWendaComments(wendaId)
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response?.errorCode == 0) {
                        val comments = response.data?.datas ?: emptyList()
                        val sortedComments = sortComments(comments)
                        _uiState.value = _uiState.value.copy(
                            comments = sortedComments,
                            isLoading = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "加载评论失败: ${response?.errorMsg}",
                            isLoading = false
                        )
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "网络错误: ${exception?.message}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "加载评论失败: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun refreshComments() {
        val wendaId = currentWendaId
        if (wendaId <= 0) return

        _uiState.value = _uiState.value.copy(
            isRefreshing = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                val result = networkRepository.getWendaComments(wendaId)
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response?.errorCode == 0) {
                        val comments = response.data?.datas ?: emptyList()
                        val sortedComments = sortComments(comments)
                        _uiState.value = _uiState.value.copy(
                            comments = sortedComments,
                            isRefreshing = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "刷新评论失败: ${response?.errorMsg}",
                            isRefreshing = false
                        )
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "网络错误: ${exception?.message}",
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "刷新评论失败: ${e.message}",
                    isRefreshing = false
                )
            }
        }
    }

    private fun sortComments(comments: List<WendaComment>): List<WendaComment> {
        val currentUserId = userManager.getUserId() ?: -1
        return comments.sortedWith(compareByDescending<WendaComment> {
            if (currentUserId > 0 && it.userId == currentUserId) 1 else 0
        }.thenByDescending { it.zan })
    }

    fun updateCommentInput(text: String) {
        _uiState.value = _uiState.value.copy(commentInput = text)
    }

    fun setReplyTarget(commentId: Int?, username: String?) {
        _uiState.value = _uiState.value.copy(
            replyToCommentId = commentId,
            replyToUsername = username
        )
    }

    fun clearReplyTarget() {
        _uiState.value = _uiState.value.copy(
            replyToCommentId = null,
            replyToUsername = null
        )
    }

    fun sendComment() {
        val currentState = _uiState.value
        val content = currentState.commentInput.trim()

        if (content.isEmpty()) return
        if (!userManager.isLoggedIn()) {
            _uiState.value = currentState.copy(
                errorMessage = "请先登录后再发表评论"
            )
            return
        }

        _uiState.value = currentState.copy(isSendingComment = true)

        viewModelScope.launch {
            try {
                android.util.Log.d(TAG, "Sending comment: $content, replyTo: ${currentState.replyToCommentId}")
                _uiState.value = _uiState.value.copy(
                    commentInput = "",
                    replyToCommentId = null,
                    replyToUsername = null,
                    isSendingComment = false
                )
                refreshComments()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "发送评论失败: ${e.message}",
                    isSendingComment = false
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun isUserLoggedIn(): Boolean {
        return userManager.isLoggedIn()
    }

    fun getCurrentUserId(): Int {
        return userManager.getUserId() ?: -1
    }
}
