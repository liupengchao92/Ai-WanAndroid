package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.ui.components.ArticleItem
import com.gradle.aicodeapp.ui.components.ArticleItemSkeleton
import com.gradle.aicodeapp.ui.components.BannerCarousel
import com.gradle.aicodeapp.ui.components.BannerSkeleton
import com.gradle.aicodeapp.ui.components.PopularCardsSection
import com.gradle.aicodeapp.ui.state.CollectUiState
import com.gradle.aicodeapp.ui.state.HomeUiState
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.theme.ResponsiveLayout
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import com.gradle.aicodeapp.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.isLoading && uiState.banners.isEmpty() && uiState.articles.isEmpty()) {
            LoadingState(paddingValues = paddingValues)
        } else {
            ContentState(
                uiState = uiState,
                collectUiState = collectUiState,
                swipeRefreshState = swipeRefreshState,
                listState = listState,
                paddingValues = paddingValues,
                viewModel = viewModel,
                collectViewModel = collectViewModel,
                onArticleClick = onArticleClick,
                onNavigateToWendaList = onNavigateToWendaList,
                onNavigateToColumnList = onNavigateToColumnList,
                onNavigateToRouteList = onNavigateToRouteList
            )
        }
    }
}

@Composable
private fun LoadingState(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(Spacing.FabSize),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = Spacing.ExtraSmall,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(Spacing.Large))
            Text(
                text = "加载中...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(Spacing.Small))
            Text(
                text = "正在获取最新内容",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentState(
    uiState: HomeUiState,
    collectUiState: CollectUiState,
    swipeRefreshState: com.google.accompanist.swiperefresh.SwipeRefreshState,
    listState: androidx.compose.foundation.lazy.LazyListState,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel,
    collectViewModel: CollectViewModel,
    onArticleClick: (String, String) -> Unit,
    onNavigateToWendaList: () -> Unit,
    onNavigateToColumnList: () -> Unit,
    onNavigateToRouteList: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ErrorSnackbars(
            uiState = uiState,
            collectUiState = collectUiState,
            viewModel = viewModel,
            collectViewModel = collectViewModel
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.refreshData() },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = ResponsiveLayout.responsiveContentPadding()
                ) {
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

                    items(
                        items = uiState.topArticles,
                        key = { article -> article.id }
                    ) {
                        article ->
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

                    items(
                        items = uiState.articles,
                        key = { article -> article.id }
                    ) {
                        article ->
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
                        LoadingFooter(
                            isLoading = uiState.isLoading,
                            hasMore = uiState.hasMore,
                            hasContent = uiState.banners.isNotEmpty() || uiState.articles.isNotEmpty()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorSnackbars(
    uiState: HomeUiState,
    collectUiState: CollectUiState,
    viewModel: HomeViewModel,
    collectViewModel: CollectViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.Small)
    ) {
        if (uiState.errorMessage != null) {
            ErrorSnackbar(
                message = uiState.errorMessage ?: "",
                onDismiss = { viewModel.clearError() }
            )
        }

        if (collectUiState.errorMessage != null) {
            ErrorSnackbar(
                message = collectUiState.errorMessage ?: "",
                onDismiss = { collectViewModel.clearError() }
            )
        }
    }
}

@Composable
private fun ErrorSnackbar(
    message: String,
    onDismiss: () -> Unit
) {
    if (message.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = Spacing.ElevationLow
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.CardPadding),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun LoadingFooter(
    isLoading: Boolean,
    hasMore: Boolean,
    hasContent: Boolean
) {
    when {
        isLoading && hasContent -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(ResponsiveLayout.responsiveHorizontalPadding()),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Spacing.Small))
                    Text(
                        text = "加载更多内容...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        !hasMore && hasContent -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(ResponsiveLayout.responsiveHorizontalPadding()),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(horizontal = Spacing.Medium)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = Spacing.Medium,
                            vertical = Spacing.Small
                        ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "·",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(Spacing.Small))
                        Text(
                            text = "已经到底了",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(Spacing.Small))
                        Text(
                            text = "·",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
