package com.gradle.aicodeapp.ui.pages


import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.network.model.PopularWenda
import com.gradle.aicodeapp.ui.components.AppTopAppBar
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.WendaListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WendaListPage(
    onBackClick: () -> Unit,
    onWendaClick: (PopularWenda) -> Unit = { },
    viewModel: WendaListViewModel = hiltViewModel(),
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

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "热门问答",
                onNavigationClick = onBackClick
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading && uiState.wendaList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(Spacing.Medium))
                    Text(
                        text = "加载中...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                            Spacer(
                                Modifier.fillMaxWidth().height(innerPadding.calculateTopPadding())
                            )
                        }

                        items(uiState.wendaList) { wenda ->
                            WendaListItem(
                                title = wenda.title,
                                author = wenda.author,
                                date = wenda.niceDate,
                                onClick = { onWendaClick(wenda) }
                            )
                        }

                        if (!uiState.isLoading && uiState.wendaList.isNotEmpty() && !uiState.hasMore) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "没有更多数据了",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (uiState.totalPages > 0) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "共 ${uiState.totalCount} 条数据，${uiState.totalPages} 页",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        if (uiState.isLoading && uiState.wendaList.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
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
  private fun WendaListItem(
    title: String,
    author: String,
    date: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.ScreenPadding, vertical = Spacing.Small)
            .clickable(onClick = onClick),
        shape = androidx.compose.material3.CardDefaults.shape,
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = Spacing.ElevationLow
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.CardPadding)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "作者：$author",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}