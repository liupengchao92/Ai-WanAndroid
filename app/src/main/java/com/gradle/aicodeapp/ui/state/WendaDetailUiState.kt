package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.PopularWenda
import com.gradle.aicodeapp.network.model.WendaComment

data class WendaDetailUiState(
    val wenda: PopularWenda? = null,
    val comments: List<WendaComment> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val commentInput: String = "",
    val isSendingComment: Boolean = false,
    val replyToCommentId: Int? = null,
    val replyToUsername: String? = null
)
