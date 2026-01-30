package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/**
 * 导航页面 - 全面UI优化版本
 * 
 * 优化特性：
 * 1. 现代简洁的视觉设计
 * 2. 左侧分组名称跑马灯效果（悬停暂停）
 * 3. 右侧标签式列表展示，随机颜色确保对比度
 * 4. 流畅的交互反馈
 * 5. 响应式设计适配
 */

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
    var isLeftClickScrolling by remember { mutableStateOf(false) }
    var isUserScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadNavigationData()
    }

    LaunchedEffect(uiState.selectedGroupIndex) {
        if (uiState.selectedGroupIndex != selectedGroupIndex) {
            selectedGroupIndex = uiState.selectedGroupIndex
        }
    }

    LaunchedEffect(rightListState.isScrollInProgress) {
        if (rightListState.isScrollInProgress) {
            isUserScrolling = true
        } else {
            kotlinx.coroutines.delay(150)
            isUserScrolling = false
        }
    }

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
            NavigationLoadingView()
        }

        uiState.errorMessage != null -> {
            NavigationErrorView(
                message = uiState.errorMessage,
                onRetry = { viewModel.loadNavigationData() }
            )
        }

        uiState.navigationGroups.isEmpty() -> {
            NavigationEmptyView()
        }

        else -> {
            NavigationContent(
                groups = uiState.navigationGroups,
                selectedGroupIndex = selectedGroupIndex,
                leftListState = leftListState,
                rightListState = rightListState,
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
                onArticleClick = onArticleClick,
                paddingValues = paddingValues
            )
        }
    }
}

@Composable
private fun NavigationLoadingView() {
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
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(Spacing.Large))
            Text(
                text = "加载中...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NavigationErrorView(
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
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.Large)
            )
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(text = "重试", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun NavigationEmptyView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "暂无导航数据",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NavigationContent(
    groups: List<com.gradle.aicodeapp.network.model.NavigationGroup>,
    selectedGroupIndex: Int,
    leftListState: LazyListState,
    rightListState: LazyListState,
    onGroupClick: (Int) -> Unit,
    onArticleClick: (String, String) -> Unit,
    paddingValues: PaddingValues,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxHeight()) {
            val screenWidth = constraints.maxWidth.dp
            val screenHeight = constraints.maxHeight.dp
            val isPortrait = screenHeight > screenWidth
            
            val navWidth = when {
                !isPortrait && screenWidth > 840.dp -> 200.dp
                !isPortrait && screenWidth > 600.dp -> 180.dp
                !isPortrait -> 160.dp
                screenWidth > 480.dp -> 120.dp
                screenWidth > 360.dp -> 100.dp
                else -> 88.dp
            }
            
            LeftNavigationPanel(
                groups = groups,
                selectedIndex = selectedGroupIndex,
                listState = leftListState,
                onGroupClick = onGroupClick,
                modifier = Modifier
                    .width(navWidth)
                    .fillMaxHeight()
                    .padding(paddingValues)
            )
        }

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )

        RightContentPanel(
            groups = groups,
            listState = rightListState,
            onArticleClick = onArticleClick,
            paddingValues = paddingValues,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun LeftNavigationPanel(
    groups: List<com.gradle.aicodeapp.network.model.NavigationGroup>,
    selectedIndex: Int,
    listState: LazyListState,
    onGroupClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val panelWidth = constraints.maxWidth.dp
        
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.background
                        )
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp)
        ) {
            itemsIndexed(
                items = groups,
                key = { index, group -> group.cid }
            ) { index, group ->
                NavigationGroupItem(
                    name = group.name,
                    isSelected = index == selectedIndex,
                    onClick = { onGroupClick(index) },
                    panelWidth = panelWidth
                )
            }
        }
    }
}

/**
 * 左侧导航分组项 - 带跑马灯效果
 * 当文字超出容器宽度时，自动滚动显示
 */
@Composable
private fun NavigationGroupItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    panelWidth: Dp,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val density = LocalDensity.current
    val panelWidthPx = with(density) { panelWidth.toPx() }
    val horizontalPaddingPx = with(density) { 32.dp.toPx() }
    val availableWidthPx = panelWidthPx - horizontalPaddingPx
    
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isPressed -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            isHovered -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else -> Color.Transparent
        },
        animationSpec = tween(200),
        label = "backgroundColor"
    )
    
    val textColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(200),
        label = "textColor"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true),
                onClick = onClick
            )
            .padding(vertical = 12.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        AutoScrollingText(
            text = name,
            color = textColor,
            isSelected = isSelected,
            availableWidth = with(density) { availableWidthPx.toDp() },
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
        )
    }
}

/**
 * 自动滚动文本组件 - 跑马灯效果
 * 当文本宽度超过可用宽度时，自动滚动
 * 鼠标悬停时暂停滚动
 */
@Composable
private fun AutoScrollingText(
    text: String,
    color: Color,
    isSelected: Boolean,
    availableWidth: Dp,
    style: androidx.compose.ui.text.TextStyle,
) {
    val density = LocalDensity.current
    val availableWidthPx = with(density) { availableWidth.toPx() }
    
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    // 使用 BoxWithConstraints 来获取容器宽度
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .hoverable(interactionSource),
        contentAlignment = Alignment.Center
    ) {
        val containerWidth = constraints.maxWidth.toFloat()
        
        // 计算文本宽度（使用 TextMeasurer 或估算）
        // 这里我们使用一个简化的方案：假设每个字符大约占一定宽度
        val charWidth = with(density) { style.fontSize.toPx() * 0.6f }
        val textWidth = text.length * charWidth
        val shouldScroll = textWidth > availableWidthPx
        val maxScroll = (textWidth - availableWidthPx + 40f).coerceAtLeast(0f)
        
        // 创建动画状态
        val infiniteTransition = rememberInfiniteTransition(label = "scroll")
        val scrollOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = if (shouldScroll) -maxScroll else 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = (textWidth * 15).toInt().coerceIn(2000, 8000),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scrollOffset"
        )
        
        val animatedOffset by animateFloatAsState(
            targetValue = if (isHovered || !shouldScroll) 0f else scrollOffset,
            animationSpec = tween(300),
            label = "animatedOffset"
        )
        
        Text(
            text = text,
            style = style,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset { IntOffset(animatedOffset.toInt(), 0) }
        )
    }
}

@Composable
private fun RightContentPanel(
    groups: List<com.gradle.aicodeapp.network.model.NavigationGroup>,
    listState: LazyListState,
    onArticleClick: (String, String) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = paddingValues.calculateTopPadding() + 8.dp,
            bottom = paddingValues.calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(
            items = groups,
            key = { index, group -> group.cid }
        ) { index, group ->
            NavigationContentGroup(
                group = group,
                onArticleClick = onArticleClick,
                groupIndex = index
            )
        }
    }
}

/**
 * 导航内容分组 - 标签式展示
 */
@Composable
private fun NavigationContentGroup(
    group: com.gradle.aicodeapp.network.model.NavigationGroup,
    onArticleClick: (String, String) -> Unit,
    groupIndex: Int,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${group.articles.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            
            TagFlowLayout(
                modifier = Modifier.fillMaxWidth(),
                horizontalSpacing = 10.dp,
                verticalSpacing = 10.dp
            ) {
                group.articles.forEachIndexed { articleIndex, article ->
                    val tagColor = rememberTagColor(article.title, groupIndex, articleIndex)
                    NavigationTagItem(
                        title = article.title,
                        onClick = { onArticleClick(article.link, article.title) },
                        backgroundColor = tagColor.backgroundColor,
                        textColor = tagColor.textColor,
                        borderColor = tagColor.borderColor
                    )
                }
            }
        }
    }
}

/**
 * 标签颜色数据类
 */
private data class TagColor(
    val backgroundColor: Color,
    val textColor: Color,
    val borderColor: Color,
)

/**
 * 生成标签颜色 - 确保对比度
 * 基于标题生成确定性的"随机"颜色
 */
@Composable
private fun rememberTagColor(title: String, groupIndex: Int, articleIndex: Int): TagColor {
    return remember(title, groupIndex, articleIndex) {
        generateTagColor(title, groupIndex, articleIndex)
    }
}

/**
 * 生成标签颜色
 * 使用预定义的颜色方案确保对比度和美观
 */
private fun generateTagColor(title: String, groupIndex: Int, articleIndex: Int): TagColor {
    val colorSchemes = listOf(
        // 蓝色系
        TagColor(
            backgroundColor = Color(0xFFE3F2FD),
            textColor = Color(0xFF1565C0),
            borderColor = Color(0xFF90CAF9)
        ),
        // 绿色系
        TagColor(
            backgroundColor = Color(0xFFE8F5E9),
            textColor = Color(0xFF2E7D32),
            borderColor = Color(0xFFA5D6A7)
        ),
        // 紫色系
        TagColor(
            backgroundColor = Color(0xFFF3E5F5),
            textColor = Color(0xFF7B1FA2),
            borderColor = Color(0xFFCE93D8)
        ),
        // 橙色系
        TagColor(
            backgroundColor = Color(0xFFFFF3E0),
            textColor = Color(0xFFEF6C00),
            borderColor = Color(0xFFFFCC80)
        ),
        // 青色系
        TagColor(
            backgroundColor = Color(0xFFE0F7FA),
            textColor = Color(0xFF00838F),
            borderColor = Color(0xFF80DEEA)
        ),
        // 粉色系
        TagColor(
            backgroundColor = Color(0xFFFCE4EC),
            textColor = Color(0xFFC2185B),
            borderColor = Color(0xFFF48FB1)
        ),
        // 靛蓝色系
        TagColor(
            backgroundColor = Color(0xFFE8EAF6),
            textColor = Color(0xFF303F9F),
            borderColor = Color(0xFF9FA8DA)
        ),
        // 茶色系
        TagColor(
            backgroundColor = Color(0xFFF1F8E9),
            textColor = Color(0xFF558B2F),
            borderColor = Color(0xFFC5E1A5)
        ),
        // 深橙色系
        TagColor(
            backgroundColor = Color(0xFFFBE9E7),
            textColor = Color(0xFFD84315),
            borderColor = Color(0xFFFFAB91)
        ),
        // 蓝灰色系
        TagColor(
            backgroundColor = Color(0xFFECEFF1),
            textColor = Color(0xFF455A64),
            borderColor = Color(0xFFB0BEC5)
        ),
        // 黄色系
        TagColor(
            backgroundColor = Color(0xFFFFFDE7),
            textColor = Color(0xFFF57F17),
            borderColor = Color(0xFFFFF59D)
        ),
        // 红色系
        TagColor(
            backgroundColor = Color(0xFFFFEBEE),
            textColor = Color(0xFFC62828),
            borderColor = Color(0xFFEF9A9A)
        ),
    )
    
    // 使用标题哈希和索引生成确定性的颜色选择
    val hash = (title.hashCode() + groupIndex * 31 + articleIndex * 17).absoluteValue
    return colorSchemes[hash % colorSchemes.size]
}

/**
 * 导航标签项
 */
@Composable
private fun NavigationTagItem(
    title: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else if (isHovered) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else if (isHovered) 4.dp else 1.dp,
        animationSpec = tween(150),
        label = "elevation"
    )
    
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) backgroundColor.copy(alpha = 0.8f) else backgroundColor,
        animationSpec = tween(150),
        label = "animatedBackgroundColor"
    )
    
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .hoverable(interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        color = animatedBackgroundColor,
        shadowElevation = elevation,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor.copy(alpha = 0.6f))
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

/**
 * 标签流式布局
 */
@Composable
private fun TagFlowLayout(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val verticalSpacingPx = verticalSpacing.roundToPx()

        val placeables = measurables.map { measurable ->
            measurable.measure(
                Constraints(
                    minWidth = 0,
                    maxWidth = constraints.maxWidth,
                    minHeight = 0,
                    maxHeight = constraints.maxHeight
                )
            )
        }

        val rows = mutableListOf<List<Placeable>>()
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0

        for (placeable in placeables) {
            val spacingNeeded = if (currentRow.isEmpty()) 0 else horizontalSpacingPx
            val wouldExceedWidth = currentRowWidth + spacingNeeded + placeable.width > constraints.maxWidth

            if (wouldExceedWidth && currentRow.isNotEmpty()) {
                rows.add(currentRow.toList())
                currentRow = mutableListOf(placeable)
                currentRowWidth = placeable.width
            } else {
                currentRow.add(placeable)
                currentRowWidth += spacingNeeded + placeable.width
            }
        }

        if (currentRow.isNotEmpty()) {
            rows.add(currentRow.toList())
        }

        val totalHeight = rows.sumOf { row ->
            row.maxOfOrNull { it.height } ?: 0
        } + (rows.size - 1) * verticalSpacingPx

        layout(
            width = constraints.maxWidth,
            height = totalHeight.coerceAtLeast(constraints.minHeight)
        ) {
            var yPosition = 0

            for (row in rows) {
                var xPosition = 0
                val rowHeight = row.maxOfOrNull { it.height } ?: 0

                for (placeable in row) {
                    placeable.place(x = xPosition, y = yPosition)
                    xPosition += placeable.width + horizontalSpacingPx
                }

                yPosition += rowHeight + verticalSpacingPx
            }
        }
    }
}
