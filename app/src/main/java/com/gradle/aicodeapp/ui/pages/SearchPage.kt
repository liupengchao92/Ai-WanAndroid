package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.data.database.SearchHistory
import com.gradle.aicodeapp.ui.components.ArticleItem
import com.gradle.aicodeapp.ui.components.HotKeyTags
import com.gradle.aicodeapp.ui.components.SearchInput
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import com.gradle.aicodeapp.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(
    viewModel: SearchViewModel,
    collectViewModel: CollectViewModel = hiltViewModel(),
    onArticleClick: (String, String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val hotKeys by viewModel.hotKeys.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()
    val collectUiState by collectViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }

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

    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            viewModel.clearResults()
        }
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom && uiState.hasMore && !uiState.isLoading) {
            viewModel.loadMore()
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)

    val scrollToTop: () -> Unit = {
        coroutineScope.launch {
            listState.animateScrollToItem(
                index = 0,
                scrollOffset = 0
            )
        }
    }

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    SearchInput(
                        modifier = Modifier.weight(1f),
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { query ->
                            viewModel.searchArticles(query)
                        },
                        onCancel = {
                            onBackClick()
                        }
                    )
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.errorMessage != null) {
                Snackbar(
                    modifier = Modifier.padding(Spacing.Small),
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
                    modifier = Modifier.padding(Spacing.Small),
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
                onRefresh = { 
                    if (searchQuery.isNotBlank()) {
                        viewModel.searchArticles(searchQuery)
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                if (uiState.isLoading && uiState.articles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(Spacing.Small))
                            Text(
                                text = "搜索中...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else if (uiState.articles.isEmpty() && !uiState.isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        if (searchQuery.isBlank()) {
                            // 显示搜索历史
                            if (searchHistory.isNotEmpty()) {
                                SearchHistoryTags(
                                    searchHistory = searchHistory,
                                    onHistoryClick = { keyword ->
                                        searchQuery = keyword
                                        viewModel.searchArticles(keyword)
                                    },
                                    onHistoryDelete = { id ->
                                        viewModel.deleteSearchHistory(id)
                                    },
                                    onClearHistory = {
                                        viewModel.clearSearchHistory()
                                    }
                                )
                            }
                            // 显示热门搜索
                            HotKeyTags(
                                hotKeys = hotKeys,
                                onHotKeyClick = { keyword ->
                                    searchQuery = keyword
                                    viewModel.searchArticles(keyword)
                                }
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
                                onClick = { onArticleClick(article.link, article.title) },
                                onCollectClick = { shouldCollect ->
                                    if (shouldCollect) {
                                        collectViewModel.collectArticle(article.id)
                                    } else {
                                        collectViewModel.uncollectArticle(article.id)
                                    }
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

@Composable
fun SearchHistoryTags(
    searchHistory: List<SearchHistory>,
    onHistoryClick: (String) -> Unit,
    onHistoryDelete: (Long) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.ScreenPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "搜索历史",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = Spacing.Medium)
            )
            TextButton(onClick = onClearHistory) {
                Text(
                    text = "清空",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            searchHistory.forEach {history ->
                SearchHistoryItem(
                    history = history,
                    onClick = { onHistoryClick(history.keyword) },
                    onDeleteClick = { onHistoryDelete(history.id) }
                )
            }
        }
    }
}

@Composable
fun SearchHistoryItem(
    history: SearchHistory,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "搜索",
            modifier = Modifier.size(Spacing.IconMedium),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(Spacing.Medium))
        Text(
            text = history.keyword,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "删除",
                modifier = Modifier.size(Spacing.IconSmall),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
