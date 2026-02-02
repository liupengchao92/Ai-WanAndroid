package com.gradle.aicodeapp.ui.pages

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.ui.components.ArticleItem
import com.gradle.aicodeapp.ui.components.ArticleItemSkeleton
import com.gradle.aicodeapp.ui.components.UnifiedSearchBox
import com.gradle.aicodeapp.ui.components.WxOfficialAccountCard
import com.gradle.aicodeapp.ui.theme.ResponsiveLayout
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import com.gradle.aicodeapp.ui.viewmodel.SquareViewModel
import kotlinx.coroutines.launch

@Composable
fun SquarePage(
    viewModel: SquareViewModel,
    collectViewModel: CollectViewModel = hiltViewModel(),
    onArticleClick: (String, String) -> Unit = { _, _ -> },
    onSearchClick: () -> Unit = {},
    onWxAccountClick: (Int, String) -> Unit = { _, _ -> },
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

    Box(modifier = Modifier.fillMaxSize()) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            // 搜索框 - 固定在顶部
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.ScreenPadding)
                    .padding(vertical = Spacing.Small)
            ) {
                UnifiedSearchBox(
                    placeholder = "搜索文章、项目、教程...",
                    onClick = onSearchClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 内容区域 - 使用 SwipeRefresh 包裹
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.refreshData() },
                modifier = Modifier.fillMaxSize()
            ) {
                if (uiState.isLoading && uiState.articles.isEmpty() && uiState.wxOfficialAccounts.isEmpty()) {
                    // 初始加载状态
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                ResponsiveLayout.responsiveContentPadding()
                            )
                    ) {
                        // 公众号骨架屏
                        WxOfficialAccountCard(
                            accounts = emptyList(),
                            isLoading = true,
                            errorMessage = null,
                            onAccountClick = {},
                            onRetryClick = {}
                        )
                        // 文章列表骨架屏
                        repeat(5) {
                            ArticleItemSkeleton()
                        }
                    }
                } else if (uiState.isRefreshing) {
                    // 刷新状态
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        contentPadding = ResponsiveLayout.responsiveContentPadding()
                    ) {
                        // 公众号卡片
                        item {
                            WxOfficialAccountCard(
                                accounts = uiState.wxOfficialAccounts,
                                isLoading = uiState.isWxAccountsLoading,
                                errorMessage = uiState.wxAccountsError,
                                onAccountClick = { account ->
                                    onWxAccountClick(account.id, account.name)
                                },
                                onRetryClick = {
                                    viewModel.refreshWxOfficialAccounts()
                                }
                            )
                        }
                        // 骨架屏
                        items(5) {
                            ArticleItemSkeleton()
                        }
                    }
                } else if (uiState.articles.isEmpty() && !uiState.isLoading) {
                    // 空数据状态
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        contentPadding = ResponsiveLayout.responsiveContentPadding()
                    ) {
                        // 公众号卡片
                        item {
                            WxOfficialAccountCard(
                                accounts = uiState.wxOfficialAccounts,
                                isLoading = uiState.isWxAccountsLoading,
                                errorMessage = uiState.wxAccountsError,
                                onAccountClick = { account ->
                                    onWxAccountClick(account.id, account.name)
                                },
                                onRetryClick = {
                                    viewModel.refreshWxOfficialAccounts()
                                }
                            )
                        }
                        // 空状态提示
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(top = Spacing.ExtraLarge)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.List,
                                        contentDescription = null,
                                        modifier = Modifier.size(Spacing.IconExtraLarge),
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
                        }
                    }
                } else {
                    // 正常显示状态 - 公众号卡片和文章列表一起滚动
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        contentPadding = ResponsiveLayout.responsiveContentPadding()
                    ) {
                        // 公众号卡片作为第一个item
                        item(key = "wx_official_accounts") {
                            WxOfficialAccountCard(
                                accounts = uiState.wxOfficialAccounts,
                                isLoading = uiState.isWxAccountsLoading,
                                errorMessage = uiState.wxAccountsError,
                                onAccountClick = { account ->
                                    onWxAccountClick(account.id, account.name)
                                },
                                onRetryClick = {
                                    viewModel.refreshWxOfficialAccounts()
                                }
                            )
                        }

                        // 文章列表
                        items(
                            items = uiState.articles,
                            key = { article -> article.id }
                        ) { article ->
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

                        // 加载更多或到底提示
                        item(key = "load_more") {
                            if (uiState.isLoading && uiState.articles.isNotEmpty()) {
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
                                            color = MaterialTheme.colorScheme.primary,
                                            strokeWidth = 2.dp,
                                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(Spacing.Small))
                                        Text(
                                            text = "加载中...",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            } else if (!uiState.hasMore && uiState.articles.isNotEmpty()) {
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
                }
            }
        }

        if (showScrollToTopButton) {
            FloatingActionButton(
                onClick = scrollToTop,
                modifier = Modifier
                    .size(Spacing.FabSizeSmall)
                    .align(Alignment.BottomEnd)
                    .padding(Spacing.ScreenPadding)
                    .padding(bottom = Spacing.ExtraHuge),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "返回顶部",
                    modifier = Modifier.size(Spacing.IconMedium)
                )
            }
        }
    }
}

@Composable
private fun ErrorSnackbar(
    message: String,
    onDismiss: () -> Unit,
) {
    if (message.isNotEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Small),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
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
private fun LoadingAnimation() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(56.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp,
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
