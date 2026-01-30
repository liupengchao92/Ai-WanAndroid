package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.model.WxOfficialAccount

/**
 * 广场UI状态
 */
data class SquareUiState(
    // 文章列表
    val articles: List<Article> = emptyList(),
    // 公众号列表
    val wxOfficialAccounts: List<WxOfficialAccount> = emptyList(),
    // 公众号加载状态
    val isWxAccountsLoading: Boolean = false,
    // 公众号加载错误信息
    val wxAccountsError: String? = null,
    // 加载状态
    val isLoading: Boolean = true,
    // 刷新状态
    val isRefreshing: Boolean = false,
    // 错误信息
    val errorMessage: String? = null,
    // 当前页码
    val currentPage: Int = 0,
    // 是否还有更多数据
    val hasMore: Boolean = true
)