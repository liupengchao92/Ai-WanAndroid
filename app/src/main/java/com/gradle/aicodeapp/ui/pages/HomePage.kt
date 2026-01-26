package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.ui.components.ArticleItem
import com.gradle.aicodeapp.ui.components.ArticleItemSkeleton
import com.gradle.aicodeapp.ui.components.BannerCarousel
import com.gradle.aicodeapp.ui.components.BannerSkeleton
import com.gradle.aicodeapp.ui.components.PopularCardsSection
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import com.gradle.aicodeapp.ui.viewmodel.HomeViewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

@Composable
fun HomePage(
    viewModel: HomeViewModel,
    collectViewModel: CollectViewModel = hiltViewModel(),
    onArticleClick: (String, String) -> Unit = { _, _ -> },
    onNavigateToWendaList: () -> Unit = {},
    onNavigateToColumnList: () -> Unit = {},
    onNavigateToRouteList: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    val collectUiState by collectViewModel.uiState.collectAsState()
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

    if (uiState.isLoading && uiState.banners.isEmpty() && uiState.articles.isEmpty()) {
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
                    Spacer(Modifier.fillMaxWidth().height(paddingValues.calculateTopPadding()))
                }

                item {
                    BannerSkeleton()
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState
                ) {
                    item {
                        Spacer(Modifier.fillMaxWidth().height(paddingValues.calculateTopPadding()))
                    }

                    item {
                        if (uiState.banners.isNotEmpty()) {
                            BannerCarousel(
                                banners = uiState.banners,
                                onBannerClick = { url -> onArticleClick(url, "") }
                            )
                        }
                    }

                    item {
                        PopularCardsSection(
                            viewModel = viewModel,
                            onWendaClick = { url, title -> onArticleClick(url, title) },
                            onColumnClick = { url, title -> onArticleClick(url, title) },
                            onRouteClick = { url, title -> onArticleClick(url, title) },
                            onViewMoreWenda = onNavigateToWendaList,
                            onViewMoreColumn = onNavigateToColumnList,
                            onViewMoreRoute = onNavigateToRouteList
                        )
                    }

                    items(uiState.topArticles) { article ->
                        ArticleItem(
                            article = article,
                            isTop = true,
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

                    items(uiState.articles) { article ->
                        ArticleItem(
                            article = article,
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
                        if (uiState.isLoading && (uiState.banners.isNotEmpty() || uiState.articles.isNotEmpty())) {
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
    }
}
