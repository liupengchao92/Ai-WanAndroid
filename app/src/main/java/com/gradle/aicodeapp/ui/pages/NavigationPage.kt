package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.ui.components.FlowLayout
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

@Composable
fun NavigationPage(
    onArticleClick: (String, String) -> Unit = { _, _ -> },
    paddingValues: PaddingValues = PaddingValues(0.dp),
    viewModel: NavigationViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val leftListState = rememberLazyListState()
    val rightListState = rememberLazyListState()

    var selectedGroupIndex by remember { mutableIntStateOf(0) }
    // 标记是否由左侧点击触发的滚动，避免循环触发
    var isLeftClickScrolling by remember { mutableStateOf(false) }
    // 记录用户手动滚动的状态
    var isUserScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadNavigationData()
    }

    LaunchedEffect(uiState.selectedGroupIndex) {
        if (uiState.selectedGroupIndex != selectedGroupIndex) {
            selectedGroupIndex = uiState.selectedGroupIndex
        }
    }

    // 监听右侧列表滚动状态，实现左右联动
    LaunchedEffect(rightListState.isScrollInProgress) {
        if (rightListState.isScrollInProgress) {
            isUserScrolling = true
        } else {
            // 滚动停止时，延迟重置状态
            kotlinx.coroutines.delay(150)
            isUserScrolling = false
        }
    }

    // 右侧列表滚动时同步更新左侧选中状态
    LaunchedEffect(rightListState.firstVisibleItemIndex) {
        if (isUserScrolling && !isLeftClickScrolling) {
            val currentIndex = rightListState.firstVisibleItemIndex
            if (currentIndex != selectedGroupIndex && currentIndex >= 0 && 
                currentIndex < uiState.navigationGroups.size) {
                selectedGroupIndex = currentIndex
                viewModel.selectGroup(currentIndex)
                coroutineScope.launch {
                    leftListState.animateScrollToItem(currentIndex)
                }
            }
        }
    }

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Spacing.Large))
                    Text(
                        text = "加载中...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
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
                BoxWithConstraints(modifier = Modifier.fillMaxHeight()) {
                    val screenWidth = constraints.maxWidth.dp
                    val screenHeight = constraints.maxHeight.dp
                    val isPortrait = screenHeight > screenWidth
                    
                    val navWidth = when {
                        !isPortrait && screenWidth > 800.dp -> 180.dp
                        !isPortrait && screenWidth > 600.dp -> 160.dp
                        !isPortrait && screenWidth > 400.dp -> 140.dp
                        isPortrait && screenWidth > 400.dp -> 100.dp
                        isPortrait -> 80.dp
                        else -> 120.dp
                    }
                    
                    LeftNavigationPanel(
                        groups = uiState.navigationGroups,
                        selectedIndex = selectedGroupIndex,
                        listState = leftListState,
                        onGroupClick = { index ->
                            if (index != selectedGroupIndex) {
                                isLeftClickScrolling = true
                                selectedGroupIndex = index
                                viewModel.selectGroup(index)
                                coroutineScope.launch {
                                    rightListState.animateScrollToItem(index)
                                    kotlinx.coroutines.delay(200)
                                    isLeftClickScrolling = false
                                }
                            }
                        },
                        modifier = Modifier
                            .width(navWidth)
                            .fillMaxHeight()
                            .padding(paddingValues)
                            .background(MaterialTheme.colorScheme.background)
                    )
                }

                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(Spacing.BorderWidthThin),
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
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val panelWidth = constraints.maxWidth.dp
        val isCompact = panelWidth <= 100.dp
        
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(if (isCompact) Spacing.ExtraSmall else Spacing.Small),
            contentPadding = PaddingValues(vertical = if (isCompact) Spacing.ExtraSmall else Spacing.Small)
        ) {
            itemsIndexed(
                items = groups,
                key = { index, group -> group.cid }
            ) { index, group ->
                NavigationGroupItem(
                    name = group.name,
                    isSelected = index == selectedIndex,
                    onClick = { onGroupClick(index) },
                    isCompact = isCompact
                )
            }
        }
    }
}

@Composable
fun NavigationGroupItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isCompact: Boolean = false,
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
    
    val horizontalPadding = if (isCompact) Spacing.ExtraSmall else Spacing.Small
    val verticalPadding = if (isCompact) Spacing.Small else Spacing.Medium
    val textStyle = if (isCompact) {
        if (isSelected) {
            MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
        } else {
            MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal)
        }
    } else {
        if (isSelected) {
            MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
        } else {
            MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = onClick
            )
            .padding(vertical = verticalPadding, horizontal = horizontalPadding)
    ) {
        Text(
            text = name,
            style = textStyle,
            color = textColor,
            maxLines = if (isCompact) 1 else 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun RightContentPanel(
    groups: List<com.gradle.aicodeapp.network.model.NavigationGroup>,
    listState: LazyListState,
    onArticleClick: (String, String) -> Unit = { _, _ -> },
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        contentPadding = paddingValues
    ) {
        itemsIndexed(
            items = groups,
            key = { index, group -> group.cid }
        ) { index, group ->
            NavigationContentGroup(
                group = group,
                onArticleClick = onArticleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.Medium)
            )
        }
    }
}

@Composable
fun NavigationContentGroup(
    group: com.gradle.aicodeapp.network.model.NavigationGroup,
    onArticleClick: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = group.name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Spacing.Medium)
        )
        FlowLayout(
            modifier = Modifier.fillMaxWidth(),
            horizontalSpacing = Spacing.Small,
            verticalSpacing = Spacing.Small
        ) {
            group.articles.forEach {
                article ->
                NavigationArticleItem(
                    article = article,
                    onClick = { onArticleClick(article.link, article.title) }
                )
            }
        }
    }
}



@Composable
fun NavigationArticleItem(
    article: com.gradle.aicodeapp.network.model.NavigationArticle,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        isPressed -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true),
                onClick = onClick
            )
            .padding(vertical = Spacing.Small, horizontal = Spacing.Medium)
    ) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.wrapContentWidth()
        )
    }

    Spacer(modifier = Modifier.height(Spacing.Small))
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            androidx.compose.material3.Button(
                onClick = onRetry,
                shape = MaterialTheme.shapes.medium,
                elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Text(text = "重试", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "暂无导航数据",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
