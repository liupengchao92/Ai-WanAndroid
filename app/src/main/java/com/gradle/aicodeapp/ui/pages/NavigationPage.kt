package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.ui.components.FlowLayout
import com.gradle.aicodeapp.ui.state.NavigationUiState
import com.gradle.aicodeapp.ui.theme.CustomShapes
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

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
                    val navWidth = when {
                        screenWidth > 800.dp -> 180.dp
                        screenWidth > 600.dp -> 160.dp
                        screenWidth > 400.dp -> 140.dp
                        else -> 120.dp
                    }
                    
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
    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small),
        contentPadding = PaddingValues(vertical = Spacing.Small)
    ) {
        itemsIndexed(
            items = groups,
            key = { index, group -> group.cid }
        ) { index, group ->
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
    onClick: () -> Unit,
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
            .padding(horizontal = Spacing.Small)
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = onClick
            )
            .padding(vertical = Spacing.Medium, horizontal = Spacing.Medium)
    ) {
        Text(
            text = name,
            style = if (isSelected) {
                MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            } else {
                MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            },
            color = textColor,
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
