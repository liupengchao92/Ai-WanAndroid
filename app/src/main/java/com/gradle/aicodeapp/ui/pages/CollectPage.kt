package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gradle.aicodeapp.R
import com.gradle.aicodeapp.navigation.NavigationArguments
import com.gradle.aicodeapp.navigation.NavigationRoutes
import com.gradle.aicodeapp.ui.components.CollectItem
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectPage(
    viewModel: CollectViewModel,
    navController: NavController,
    onArticleClick: (String, String) -> Unit = { _, _ -> },
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var articleToDelete by remember { mutableStateOf<Int?>(null) }
    var collectIdToDelete by remember { mutableStateOf<Int?>(null) }

    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadCollectList()
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom && uiState.hasMore && !uiState.isLoading) {
            viewModel.loadMore()
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的收藏") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF0F0F0),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { scaffoldPadding ->
        if (uiState.isLoading && uiState.articles.isEmpty()) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding),
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
                            Spacer(Modifier.fillMaxWidth().height(scaffoldPadding.calculateTopPadding()))
                        }

                        items(uiState.articles) { article ->
                            CollectItem(
                                article = article,
                                onClick = { onArticleClick(article.link, article.title) },
                                onEditClick = {
                                    val encodedTitle = URLEncoder.encode(article.title, StandardCharsets.UTF_8.toString())
                                    val encodedAuthor = URLEncoder.encode(article.author ?: "", StandardCharsets.UTF_8.toString())
                                    val encodedLink = URLEncoder.encode(article.link, StandardCharsets.UTF_8.toString())
                                    navController.navigate("${NavigationRoutes.COLLECT_EDIT}/${article.id}/$encodedTitle/$encodedAuthor/$encodedLink")
                                },
                                onDeleteClick = {
                                    articleToDelete = article.id
                                    collectIdToDelete = article.id
                                    showDeleteDialog = true
                                }
                            )
                        }

                        item {
                            if (uiState.isLoading && uiState.articles.isNotEmpty()) {
                                androidx.compose.foundation.layout.Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                    Text(text = "加载中...", modifier = Modifier.padding(top = 8.dp))
                                }
                            } else if (!uiState.hasMore && uiState.articles.isNotEmpty()) {
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

                FloatingActionButton(
                    onClick = {
                        navController.navigate(NavigationRoutes.COLLECT_ADD)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "添加收藏"
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("取消收藏") },
            text = { Text("确定要取消收藏这篇文章吗？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        if (articleToDelete != null) {
                            viewModel.uncollectArticle(articleToDelete!!)
                        }
                        if (collectIdToDelete != null) {
                            viewModel.uncollectOutsideArticle(collectIdToDelete!!)
                        }
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("取消")
                }
            }
        )
    }
}
