package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.Article

/**
 * 公众号文章列表UI状态
 */
data class WxArticleListUiState(
    // 文章列表
    val articles: List<Article> = emptyList(),
    // 加载状态
    val isLoading: Boolean = true,
    // 刷新状态
    val isRefreshing: Boolean = false,
    // 错误信息
    val errorMessage: String? = null,
    // 当前页码（从1开始）
    val currentPage: Int = 1,
    // 是否还有更多数据
    val hasMore: Boolean = true,
    // 公众号ID
    val accountId: Int = 0,
    // 公众号名称
    val accountName: String = ""
)