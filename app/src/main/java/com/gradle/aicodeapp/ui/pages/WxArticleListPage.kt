package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.ui.components.ArticleItem
import com.gradle.aicodeapp.ui.components.ArticleItemSkeleton
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import com.gradle.aicodeapp.ui.viewmodel.WxArticleListViewModel
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.theme.ResponsiveLayout
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WxArticleListPage(
    accountId: Int,
    accountName: String,
    viewModel: WxArticleListViewModel = hiltViewModel(),
    collectViewModel: CollectViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onArticleClick: (String, String) -> Unit = { _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsState()
    val collectUiState by collectViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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

    LaunchedEffect(key1 = accountId) {
        viewModel.initData(accountId, accountName)
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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = uiState.accountName.ifEmpty { "公众号文章" })
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = scrollToTop,
                    modifier = Modifier.size(Spacing.FabSizeSmall),
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            // 错误提示
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

            // 加载中状态
            if (uiState.isLoading && uiState.articles.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 显示文章列表骨架屏
                    repeat(5) {
                        ArticleItemSkeleton()
                    }
                }
            } else {
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { viewModel.refreshData() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (uiState.isRefreshing) {
                        // 显示文章列表骨架屏
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                            contentPadding = ResponsiveLayout.responsiveContentPadding()
                        ) {
                            items(5) {
                                ArticleItemSkeleton()
                            }
                        }
                    } else if (uiState.articles.isEmpty() && !uiState.isLoading) {
                        // 空数据状态
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
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
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                            contentPadding = ResponsiveLayout.responsiveContentPadding()
                        ) {
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
                                        viewModel.updateArticleCollectStatus(
                                            article.id,
                                            shouldCollect
                                        )
                                    }
                                )
                            }

                            // 加载更多或到底提示
                            item {
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
        }
    }
}

@Composable
private fun ErrorSnackbar(
    message: String,
    onDismiss: () -> Unit,
) {
    if (message.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium)
        ) {
            Snackbar(
                modifier = Modifier.fillMaxWidth(),
                action = {
                    TextButton(onClick = onDismiss) {
                        Text("关闭")
                    }
                }
            ) {
                Text(text = message)
            }
        }
    }
}