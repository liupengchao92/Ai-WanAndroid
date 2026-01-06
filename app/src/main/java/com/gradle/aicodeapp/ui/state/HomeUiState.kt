package com.gradle.aicodeapp.ui.state

import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.network.model.Banner

/**
 * 首页UI状态
 */
data class HomeUiState(
    // Banner数据
    val banners: List<Banner> = emptyList(),
    // 置顶文章
    val topArticles: List<Article> = emptyList(),
    // 普通文章
    val articles: List<Article> = emptyList(),
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
