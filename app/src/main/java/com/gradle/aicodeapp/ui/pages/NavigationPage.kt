package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.ui.state.NavigationUiState
import com.gradle.aicodeapp.ui.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

@Composable
fun NavigationPage(
    onArticleClick: (String) -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val leftListState = rememberLazyListState()
    val rightListState = rememberLazyListState()
    
    var selectedGroupIndex by remember { mutableIntStateOf(0) }
    var isScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadNavigationData()
    }

    LaunchedEffect(uiState.selectedGroupIndex) {
        if (uiState.selectedGroupIndex != selectedGroupIndex) {
            selectedGroupIndex = uiState.selectedGroupIndex
        }
    }

    LaunchedEffect(selectedGroupIndex) {
        if (!isScrolling && selectedGroupIndex >= 0 && selectedGroupIndex < uiState.navigationGroups.size) {
            coroutineScope.launch {
                rightListState.animateScrollToItem(selectedGroupIndex)
            }
        }
    }

    val visibleGroupIndex by remember {
        derivedStateOf {
            val layoutInfo = rightListState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isNotEmpty()) {
                visibleItems.first().index
            } else {
                -1
            }
        }
    }

    LaunchedEffect(visibleGroupIndex) {
        if (visibleGroupIndex >= 0 && visibleGroupIndex != selectedGroupIndex && !isScrolling) {
            selectedGroupIndex = visibleGroupIndex
            viewModel.selectGroup(visibleGroupIndex)
            coroutineScope.launch {
                leftListState.animateScrollToItem(visibleGroupIndex)
            }
        }
    }

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null -> {
            ErrorView(
                message = uiState.errorMessage,
                onRetry = { viewModel.loadNavigationData() }
            )
        }
        uiState.navigationGroups.isEmpty() -> {
            EmptyView()
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                LeftNavigationPanel(
                    groups = uiState.navigationGroups,
                    selectedIndex = selectedGroupIndex,
                    listState = leftListState,
                    onGroupClick = { index ->
                        isScrolling = true
                        selectedGroupIndex = index
                        viewModel.selectGroup(index)
                        coroutineScope.launch {
                            rightListState.animateScrollToItem(index)
                            isScrolling = false
                        }
                    },
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight()
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.surface)
                )

                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                RightContentPanel(
                    groups = uiState.navigationGroups,
                    listState = rightListState,
                    onArticleClick = onArticleClick,
                    paddingValues = paddingValues,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun LeftNavigationPanel(
    groups: List<com.gradle.aicodeapp.network.model.NavigationGroup>,
    selectedIndex: Int,
    listState: LazyListState,
    onGroupClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(groups) { index, group ->
            NavigationGroupItem(
                name = group.name,
                isSelected = index == selectedIndex,
                onClick = { onGroupClick(index) }
            )
        }
    }
}

@Composable
fun NavigationGroupItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun RightContentPanel(
    groups: List<com.gradle.aicodeapp.network.model.NavigationGroup>,
    listState: LazyListState,
    onArticleClick: (String) -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = paddingValues
    ) {
        itemsIndexed(groups) { index, group ->
            NavigationContentGroup(
                group = group,
                onArticleClick = onArticleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun NavigationContentGroup(
    group: com.gradle.aicodeapp.network.model.NavigationGroup,
    onArticleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = group.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        group.articles.forEach { article ->
            NavigationArticleItem(
                article = article,
                onClick = { onArticleClick(article.link) }
            )
        }
    }
}

@Composable
fun NavigationArticleItem(
    article: com.gradle.aicodeapp.network.model.NavigationArticle,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 12.dp)
    ) {
        Column {
            Text(
                text = article.title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            if (!article.desc.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = article.desc,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
            androidx.compose.material3.Button(onClick = onRetry) {
                Text("重试")
            }
        }
    }
}

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "暂无导航数据",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontSize = 14.sp
        )
    }
}
