package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.data.database.SearchHistory
import com.gradle.aicodeapp.network.model.Article
import com.gradle.aicodeapp.ui.components.ArticleItem
import com.gradle.aicodeapp.ui.components.UnifiedSearchInput
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.theme.ResponsiveLayout
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import com.gradle.aicodeapp.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val haptic = LocalHapticFeedback.current
    val focusRequester = remember { FocusRequester() }

    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }

    val horizontalPadding = ResponsiveLayout.calculateHorizontalPadding()

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

    val hasSearchResults = uiState.articles.isNotEmpty()
    val showInitialContent = searchQuery.isBlank() && !hasSearchResults && !uiState.isLoading

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

    LaunchedEffect(uiState.errorMessage, collectUiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
        collectUiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            collectViewModel.clearError()
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading && hasSearchResults)

    val scrollToTop: () -> Unit = {
        coroutineScope.launch {
            listState.animateScrollToItem(index = 0, scrollOffset = 0)
        }
    }

    val performSearch: (String) -> Unit = { query ->
        if (query.isNotBlank()) {
            keyboardController?.hide()
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            viewModel.searchArticles(query.trim())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    UnifiedSearchInput(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = Spacing.Medium),
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = performSearch,
                        onClear = { searchQuery = "" },
                        focusRequester = focusRequester,
                        onFocusChange = { isSearchFocused = it }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.padding(Spacing.ScreenPadding),
                    action = {
                        TextButton(onClick = { data.dismiss() }) {
                            Text("关闭")
                        }
                    }
                ) {
                    Text(data.visuals.message)
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = uiState.isLoading && !hasSearchResults,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
                },
                label = "loading_content"
            ) { isInitialLoading ->
                if (isInitialLoading) {
                    ModernLoadingState()
                } else {
                    SwipeRefresh(
                        state = swipeRefreshState,
                        onRefresh = {
                            if (searchQuery.isNotBlank()) {
                                viewModel.searchArticles(searchQuery)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when {
                            hasSearchResults -> {
                                SearchResultsList(
                                    articles = uiState.articles,
                                    isLoadingMore = uiState.isLoading && hasSearchResults,
                                    hasMore = uiState.hasMore,
                                    listState = listState,
                                    paddingValues = paddingValues,
                                    horizontalPadding = horizontalPadding,
                                    onArticleClick = onArticleClick,
                                    onCollectClick = { article, shouldCollect ->
                                        if (shouldCollect) {
                                            collectViewModel.collectArticle(article.id)
                                        } else {
                                            collectViewModel.uncollectArticle(article.id)
                                        }
                                    }
                                )
                            }
                            showInitialContent -> {
                                InitialSearchContent(
                                    searchHistory = searchHistory,
                                    hotKeys = hotKeys,
                                    paddingValues = paddingValues,
                                    horizontalPadding = horizontalPadding,
                                    onHistoryClick = { keyword ->
                                        searchQuery = keyword
                                        performSearch(keyword)
                                    },
                                    onHistoryDelete = { id ->
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.deleteSearchHistory(id)
                                    },
                                    onClearHistory = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.clearSearchHistory()
                                    },
                                    onHotKeyClick = { keyword ->
                                        searchQuery = keyword
                                        performSearch(keyword)
                                    }
                                )
                            }
                            else -> {
                                EmptySearchResult(
                                    query = searchQuery,
                                    paddingValues = paddingValues,
                                    onClearSearch = { searchQuery = "" }
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showScrollToTopButton,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessMedium)
                ) + fadeIn(animationSpec = tween(200)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessMedium)
                ) + fadeOut(animationSpec = tween(200)),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = Spacing.Large)
                    .padding(bottom = paddingValues.calculateBottomPadding() + Spacing.Large)
            ) {
                FloatingActionButton(
                    onClick = scrollToTop,
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InitialSearchContent(
    searchHistory: List<SearchHistory>,
    hotKeys: List<com.gradle.aicodeapp.network.model.Friend>,
    paddingValues: PaddingValues,
    horizontalPadding: androidx.compose.ui.unit.Dp,
    onHistoryClick: (String) -> Unit,
    onHistoryDelete: (Long) -> Unit,
    onClearHistory: () -> Unit,
    onHotKeyClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + Spacing.Medium,
            bottom = paddingValues.calculateBottomPadding() + Spacing.Large,
            start = horizontalPadding,
            end = horizontalPadding
        )
    ) {
        if (searchHistory.isNotEmpty()) {
            item {
                SearchHistorySection(
                    searchHistory = searchHistory,
                    onHistoryClick = onHistoryClick,
                    onHistoryDelete = onHistoryDelete,
                    onClearHistory = onClearHistory
                )

                Spacer(modifier = Modifier.height(Spacing.ExtraLarge))
            }
        }

        item {
            HotSearchSection(
                hotKeys = hotKeys,
                onHotKeyClick = onHotKeyClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(Spacing.Huge))

            SearchTipsSection()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchHistorySection(
    searchHistory: List<SearchHistory>,
    onHistoryClick: (String) -> Unit,
    onHistoryDelete: (Long) -> Unit,
    onClearHistory: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(Spacing.Small))

                Text(
                    text = "搜索历史",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            TextButton(
                onClick = onClearHistory,
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "清空",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.Medium))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            searchHistory.forEach { history ->
                HistoryChip(
                    history = history,
                    onClick = { onHistoryClick(history.keyword) },
                    onDelete = { onHistoryDelete(history.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryChip(
    history: SearchHistory,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    InputChip(
        onClick = onClick,
        label = {
            Text(
                text = history.keyword,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        selected = false,
        modifier = Modifier.height(36.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "删除",
                    modifier = Modifier.size(14.dp)
                )
            }
        },
        colors = InputChipDefaults.inputChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HotSearchSection(
    hotKeys: List<com.gradle.aicodeapp.network.model.Friend>,
    onHotKeyClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.width(Spacing.Small))

            Text(
                text = "热门搜索",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(Spacing.Medium))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            hotKeys.take(10).forEachIndexed { index, hotKey ->
                HotSearchChip(
                    text = hotKey.name,
                    rank = index + 1,
                    onClick = { onHotKeyClick(hotKey.name) }
                )
            }
        }
    }
}

@Composable
private fun HotSearchChip(
    text: String,
    rank: Int,
    onClick: () -> Unit
) {
    val rankColor = when (rank) {
        1 -> MaterialTheme.colorScheme.error
        2 -> MaterialTheme.colorScheme.tertiary
        3 -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val backgroundColor = when (rank) {
        1 -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        2 -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        3 -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = Modifier.height(36.dp),
        leadingIcon = {
            Text(
                text = rank.toString(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = rankColor
                )
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = backgroundColor,
            labelColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun SearchTipsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(Spacing.CornerRadiusLarge)
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Large)
        ) {
            Text(
                text = "搜索技巧",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            SearchTipItem(
                icon = Icons.Default.Search,
                text = "输入关键词快速查找文章"
            )
            SearchTipItem(
                icon = Icons.Default.Search,
                text = "点击热门标签发现优质内容"
            )
            SearchTipItem(
                icon = Icons.Default.Search,
                text = "长按历史记录可快速删除"
            )
        }
    }
}

@Composable
private fun SearchTipItem(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier.padding(vertical = Spacing.ExtraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.width(Spacing.Small))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun SearchResultsList(
    articles: List<Article>,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    listState: androidx.compose.foundation.lazy.LazyListState,
    paddingValues: PaddingValues,
    horizontalPadding: androidx.compose.ui.unit.Dp,
    onArticleClick: (String, String) -> Unit,
    onCollectClick: (Article, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding() + Spacing.Large,
            start = horizontalPadding,
            end = horizontalPadding
        )
    ) {
        items(
            items = articles,
            key = { it.id }
        ) { article ->
            ArticleItem(
                article = article,
                onClick = { onArticleClick(article.link, article.title) },
                onCollectClick = { shouldCollect ->
                    onCollectClick(article, shouldCollect)
                }
            )
        }

        item {
            when {
                isLoadingMore -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.Large),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
                !hasMore && articles.isNotEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.Large),
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

@Composable
private fun EmptySearchResult(
    query: String,
    paddingValues: PaddingValues,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = Spacing.ScreenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(Spacing.ExtraLarge))

        Text(
            text = "未找到相关结果",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Spacing.Small))

        Text(
            text = "换个关键词试试？",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(Spacing.ExtraLarge))

        TextButton(
            onClick = onClearSearch,
            colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "清除搜索",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun ModernLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 3.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(Spacing.Large))

            Text(
                text = "搜索中...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

