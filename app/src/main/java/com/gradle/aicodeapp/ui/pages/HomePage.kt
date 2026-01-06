package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.ui.components.ArticleItem
import com.gradle.aicodeapp.ui.components.BannerCarousel
import com.gradle.aicodeapp.ui.viewmodel.HomeViewModel

@Composable
fun HomePage(
    viewModel: HomeViewModel
) {
    // 收集状态
    val uiState by viewModel.uiState.collectAsState()

    // 初始化加载
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
    }

    // 主布局
    if (uiState.isLoading && uiState.banners.isEmpty() && uiState.articles.isEmpty()) {
        // 初始加载状态
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text(text = "加载中...", modifier = Modifier.padding(top = 16.dp))
        }
    } else {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 错误提示
            if (uiState.errorMessage != null) {
                androidx.compose.material3.Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        androidx.compose.material3.TextButton(
                            onClick = { viewModel.clearError() }
                        ) {
                            Text(text = "关闭")
                        }
                    }
                ) {
                    Text(text = uiState.errorMessage ?: "")
                }
            }

            // 内容列表
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Banner轮播图
                item {
                    if (uiState.banners.size > 0) {
                        BannerCarousel(banners = uiState.banners)
                    }
                }

                // 置顶文章
                items(uiState.topArticles) { article ->
                    ArticleItem(
                        article = article,
                        isTop = true,
                        onClick = { /* 处理点击事件 */ }
                    )
                }

                // 普通文章
                items(uiState.articles) { article ->
                    ArticleItem(
                        article = article,
                        onClick = { /* 处理点击事件 */ }
                    )
                }

                // 加载更多
                item {
                    if (!uiState.isLoading) {
                        if (uiState.hasMore) {
                            androidx.compose.foundation.layout.Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                androidx.compose.material3.Button(
                                    onClick = { viewModel.loadMore() }
                                ) {
                                    Text(text = "加载更多")
                                }
                            }
                        } else {
                            androidx.compose.foundation.layout.Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "没有更多数据了", modifier = Modifier.padding(top = 8.dp))
                            }
                        }
                    } else if (uiState.banners.size > 0 || uiState.articles.size > 0) {
                        // 加载更多状态
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Text(text = "加载中...", modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}
