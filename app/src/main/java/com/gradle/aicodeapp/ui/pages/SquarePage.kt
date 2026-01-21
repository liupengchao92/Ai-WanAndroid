package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.ui.components.ArticleItem
import com.gradle.aicodeapp.ui.components.ArticleItemSkeleton
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import com.gradle.aicodeapp.ui.viewmodel.SquareViewModel
import com.gradle.aicodeapp.ui.theme.Spacing
import kotlinx.coroutines.launch

@Composable
fun SquarePage(
    viewModel: SquareViewModel,
    collectViewModel: CollectViewModel = hiltViewModel(),
    onArticleClick: (String, String) -> Unit = { _, _ -> },
    paddingValues: PaddingValues = PaddingValues(0.dp),
) {
    val uiState by viewModel.uiState.collectAsState()
    val collectUiState by collectViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    val showScrollToTopButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 200
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

    val scrollToTop: () -> Unit = {
        coroutineScope.launch {
            listState.animateScrollToItem(
                index = 0,
                scrollOffset = 0
            )
        }
    }

    if (uiState.isLoading && uiState.articles.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                item {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(paddingValues.calculateTopPadding())
                    )
                }

                items(5) {
                    ArticleItemSkeleton()
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.errorMessage != null) {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        TextButton(
                            onClick = { viewModel.clearError() }
                        ) {
                            Text(text = "关闭")
                        }
                    }
                ) {
                    Text(text = uiState.errorMessage ?: "")
                }
            }

            if (collectUiState.errorMessage != null) {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        TextButton(
                            onClick = { collectViewModel.clearError() }
                        ) {
                            Text(text = "关闭")
                        }
                    }
                ) {
                    Text(text = collectUiState.errorMessage ?: "")
                }
            }

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.refreshData() },
                modifier = Modifier.fillMaxSize()
            ) {
                if (uiState.isLoading && uiState.articles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.layout.Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Text(text = "加载中...", modifier = Modifier.padding(top = 16.dp))
                        }
                    }
                } else if (uiState.articles.isEmpty() && !uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.layout.Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(Spacing.Medium))
                            Text(
                                text = "暂无文章",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = listState
                    ) {
                        item {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .height(paddingValues.calculateTopPadding())
                            )
                        }

                        items(uiState.articles) { article ->
                            ArticleItem(
                                article = article,
                                isSquare = true,
                                onClick = { onArticleClick(article.link, article.title) },
                                onCollectClick = { shouldCollect ->
                                    if (shouldCollect) {
                                        collectViewModel.collectArticle(article.id)
                                    } else {
                                        collectViewModel.uncollectArticle(article.id)
                                    }
                                    viewModel.updateArticleCollectStatus(article.id, shouldCollect)
                                }
                            )
                        }

                        item {
                            if (uiState.isLoading && uiState.articles.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else if (!uiState.hasMore && uiState.articles.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "没有更多数据了",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeOut(animationSpec = tween(durationMillis = 300)),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(bottom = 80.dp)
            ) {
                FloatingActionButton(
                    onClick = scrollToTop,
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "返回顶部",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
