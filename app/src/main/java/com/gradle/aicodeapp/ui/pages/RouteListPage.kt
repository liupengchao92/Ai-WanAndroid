package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.gradle.aicodeapp.ui.components.AppTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.R
import com.gradle.aicodeapp.network.model.PopularRoute
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.RouteListViewModel
import androidx.compose.foundation.layout.PaddingValues

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteListPage(
    onBackClick: () -> Unit,
    onRouteClick: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    viewModel: RouteListViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing)

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "学习路线",
                onNavigationClick = onBackClick
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading && uiState.routeList.isEmpty()) {
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

                        items(uiState.routeList) { route ->
                            RouteListItem(
                                name = route.name,
                                author = route.author,
                                desc = route.desc,
                                onClick = { onRouteClick("https://www.wanandroid.com/show/${route.id}", route.name) }
                            )
                        }

                        if (!uiState.isLoading && uiState.routeList.isNotEmpty() && !uiState.hasMore) {
                            item {
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

                        if (uiState.isLoading && uiState.routeList.isNotEmpty()) {
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
private fun RouteListItem(
    name: String,
    author: String,
    desc: String,
    onClick: () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.ScreenPadding, vertical = Spacing.Small),
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
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            Text(
                text = "作者：$author",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Spacing.Small))

            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}