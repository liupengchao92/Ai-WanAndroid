package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.ui.components.ArticleItem
import com.gradle.aicodeapp.ui.viewmodel.SquareViewModel
import androidx.compose.foundation.layout.PaddingValues

@Composable
fun SquarePage(
    viewModel: SquareViewModel,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom && uiState.hasMore && !uiState.isLoading) {
            viewModel.loadMore()
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing)

    if (uiState.isLoading && uiState.articles.isEmpty()) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text(text = "加载中...", modifier = Modifier.padding(top = 16.dp))
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
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

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.refreshData() },
                modifier = Modifier.fillMaxSize()
            ) {
                if (uiState.articles.isEmpty() && !uiState.isLoading) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "暂无文章",
                            modifier = Modifier.padding(top = 16.dp),
                            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        state = listState
                    ) {
                        items(uiState.articles) { article ->
                            ArticleItem(
                                article = article,
                                onClick = { 
                                    println("点击文章: ${article.title}")
                                }
                            )
                        }

                        item {
                            if (uiState.isLoading && uiState.articles.isNotEmpty()) {
                                androidx.compose.foundation.layout.Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                    Text(text = "加载中...", modifier = Modifier.padding(top = 8.dp))
                                }
                            } else if (!uiState.hasMore && uiState.articles.isNotEmpty()) {
                                androidx.compose.foundation.layout.Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "没有更多数据了", modifier = Modifier.padding(top = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
