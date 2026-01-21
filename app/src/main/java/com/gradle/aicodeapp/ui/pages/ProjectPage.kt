package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.ui.components.ProjectItem
import com.gradle.aicodeapp.ui.viewmodel.ProjectViewModel

@Composable
fun ProjectPage(
    viewModel: ProjectViewModel,
    onArticleClick: (String, String) -> Unit = { _, _ -> },
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadCategories()
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom && uiState.hasMore && !uiState.isLoading) {
            viewModel.loadMore()
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing)

    if (uiState.isLoading && uiState.categories.isEmpty()) {
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState
                ) {
                    item {
                        Spacer(Modifier.fillMaxWidth().height(paddingValues.calculateTopPadding()))
                    }

                    item {
                        ProjectCategoryTabs(
                            categories = uiState.categories,
                            selectedIndex = uiState.selectedCategoryIndex,
                            onCategorySelected = { index ->
                                viewModel.selectCategory(index)
                            }
                        )
                    }

                    items(uiState.projects) { project ->
                        ProjectItem(
                            article = project,
                            onClick = { onArticleClick(project.link, project.title) }
                        )
                    }

                    item {
                        if (uiState.isLoading && uiState.projects.isNotEmpty()) {
                            androidx.compose.foundation.layout.Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Text(text = "加载中...", modifier = Modifier.padding(top = 8.dp))
                            }
                        } else if (!uiState.hasMore && uiState.projects.isNotEmpty()) {
                            androidx.compose.foundation.layout.Column(
                                modifier = Modifier
                                    .fillMaxWidth()
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

@Composable
fun ProjectCategoryTabs(
    categories: List<com.gradle.aicodeapp.network.model.ProjectCategory>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                color = MaterialTheme.colorScheme.primary
            )
        },
        divider = {},
        edgePadding = 16.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onCategorySelected(index) },
                text = {
                    Text(
                        text = category.name,
                        color = if (selectedIndex == index) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            )
        }
    }
}
