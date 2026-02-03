package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.gradle.aicodeapp.ui.components.CenteredLoadingView
import com.gradle.aicodeapp.ui.components.FullScreenLoadingView
import com.gradle.aicodeapp.ui.components.InlineLoadingView
import com.gradle.aicodeapp.ui.components.LoadingSize
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.network.model.Todo
import com.gradle.aicodeapp.ui.components.AppTopAppBar
import com.gradle.aicodeapp.ui.state.TodoUiState
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoPage(
    viewModel: TodoViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToAdd: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= layoutInfo.totalItemsCount - 3
        }
    }

    val completedCount = uiState.todos.count { it.isCompleted() }
    val totalCount = uiState.todos.size
    val completionRate = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !uiState.isLoading && uiState.hasMore) {
            viewModel.loadMore()
        }
    }

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            viewModel.refreshData()
            pullToRefreshState.endRefresh()
        }
    }

    LaunchedEffect(Unit) {
        if (uiState.todos.isEmpty()) {
            viewModel.loadTodoList()
        }
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "待办事项",
                onNavigationClick = onBackClick,
                actions = {
                    FilterMenu(
                        filterStatus = uiState.filterStatus,
                        filterType = uiState.filterType,
                        filterPriority = uiState.filterPriority,
                        orderby = uiState.orderby,
                        onFilterStatusChange = { viewModel.setFilterStatus(it) },
                        onFilterTypeChange = { viewModel.setFilterType(it) },
                        onFilterPriorityChange = { viewModel.setFilterPriority(it) },
                        onOrderByChange = { viewModel.setOrderBy(it) }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 12.dp
                ),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加待办",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading && uiState.todos.isEmpty() -> {
                    TodoLoadingContent()
                }
                uiState.errorMessage != null && uiState.todos.isEmpty() -> {
                    val errorMessage = uiState.errorMessage ?: "未知错误"
                    ErrorContent(
                        message = errorMessage,
                        onRetry = { viewModel.loadTodoList() }
                    )
                }
                uiState.todos.isEmpty() -> {
                    EmptyContent(onAddClick = onNavigateToAdd)
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = Spacing.ScreenPadding,
                            vertical = Spacing.Medium
                        ),
                        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                    ) {
                        // 统计卡片
                        item(key = "stats") {
                            StatsCard(
                                totalCount = totalCount,
                                completedCount = completedCount,
                                completionRate = completionRate
                            )
                        }

                        // 筛选指示器
                        if (uiState.filterStatus != null || uiState.filterType != null || uiState.filterPriority != null) {
                            item(key = "filters") {
                                ActiveFiltersBar(
                                    filterStatus = uiState.filterStatus,
                                    filterType = uiState.filterType,
                                    filterPriority = uiState.filterPriority,
                                    onClearFilters = {
                                        viewModel.setFilterStatus(null)
                                        viewModel.setFilterType(null)
                                        viewModel.setFilterPriority(null)
                                    }
                                )
                            }
                        }

                        items(
                            items = uiState.todos,
                            key = { it.id }
                        ) { todo ->
                            TodoItem(
                                todo = todo,
                                onClick = { onNavigateToEdit(todo.id) },
                                onStatusToggle = { viewModel.toggleTodoStatus(todo) },
                                onDelete = { viewModel.showDeleteDialog(todo) },
                                dateFormat = dateFormat
                            )
                        }
                        if (uiState.isLoading) {
                            item {
                                InlineLoadingView(
                                    size = LoadingSize.SMALL
                                )
                            }
                        }
                    }
                }
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

    if (uiState.showDeleteDialog && uiState.todoToDelete != null) {
        DeleteConfirmDialog(
            todo = uiState.todoToDelete!!,
            onConfirm = { viewModel.deleteTodo(uiState.todoToDelete!!) },
            onDismiss = { viewModel.hideDeleteDialog() }
        )
    }
}

@Composable
fun StatsCard(
    totalCount: Int,
    completedCount: Int,
    completionRate: Float
) {
    val animatedProgress by animateFloatAsState(
        targetValue = completionRate,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "progress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.Small),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "今日概览",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(Spacing.ExtraSmall))
                    Text(
                        text = "${completedCount}/${totalCount} 已完成",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 圆形进度指示器
                Box(
                    modifier = Modifier.size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 6.dp,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 6.dp,
                        trackColor = Color.Transparent
                    )
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Medium))

            // 进度条
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun ActiveFiltersBar(
    filterStatus: Int?,
    filterType: Int?,
    filterPriority: Int?,
    onClearFilters: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            modifier = Modifier.weight(1f)
        ) {
            filterStatus?.let {
                FilterTag(
                    text = getStatusName(it),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            filterType?.let {
                FilterTag(
                    text = getTypeName(it),
                    color = getTypeColor(it)
                )
            }
            filterPriority?.let {
                FilterTag(
                    text = getPriorityName(it),
                    color = getPriorityColor(it)
                )
            }
        }

        TextButton(
            onClick = onClearFilters,
            modifier = Modifier.padding(start = Spacing.Small)
        ) {
            Text(
                text = "清除筛选",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun FilterTag(
    text: String,
    color: Color
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(28.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
        }
    }
}

@Composable
fun FilterMenu(
    filterStatus: Int?,
    filterType: Int?,
    filterPriority: Int?,
    orderby: Int?,
    onFilterStatusChange: (Int?) -> Unit,
    onFilterTypeChange: (Int?) -> Unit,
    onFilterPriorityChange: (Int?) -> Unit,
    onOrderByChange: (Int?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val hasActiveFilters = filterStatus != null || filterType != null || filterPriority != null

    Box {
        IconButton(onClick = { expanded = true }) {
            Box {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "筛选",
                    tint = if (hasActiveFilters) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                if (hasActiveFilters) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .offset(x = 8.dp, y = (-4).dp)
                            .background(
                                color = MaterialTheme.colorScheme.error,
                                shape = CircleShape
                            )
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { FilterMenuItemText("状态", getStatusName(filterStatus)) },
                onClick = {
                    expanded = false
                    when (filterStatus) {
                        null -> onFilterStatusChange(TodoUiState.STATUS_INCOMPLETE)
                        TodoUiState.STATUS_INCOMPLETE -> onFilterStatusChange(TodoUiState.STATUS_COMPLETED)
                        TodoUiState.STATUS_COMPLETED -> onFilterStatusChange(null)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            DropdownMenuItem(
                text = { FilterMenuItemText("类型", getTypeName(filterType)) },
                onClick = {
                    expanded = false
                    when (filterType) {
                        null -> onFilterTypeChange(Todo.TYPE_WORK)
                        Todo.TYPE_WORK -> onFilterTypeChange(Todo.TYPE_LIFE)
                        Todo.TYPE_LIFE -> onFilterTypeChange(Todo.TYPE_ENTERTAINMENT)
                        Todo.TYPE_ENTERTAINMENT -> onFilterTypeChange(null)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            DropdownMenuItem(
                text = { FilterMenuItemText("优先级", getPriorityName(filterPriority)) },
                onClick = {
                    expanded = false
                    when (filterPriority) {
                        null -> onFilterPriorityChange(Todo.PRIORITY_HIGH)
                        Todo.PRIORITY_HIGH -> onFilterPriorityChange(Todo.PRIORITY_NORMAL)
                        Todo.PRIORITY_NORMAL -> onFilterPriorityChange(null)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            DropdownMenuItem(
                text = { FilterMenuItemText("排序", getOrderName(orderby)) },
                onClick = {
                    expanded = false
                    when (orderby) {
                        null -> onOrderByChange(TodoUiState.ORDERBY_CREATE_ASC)
                        TodoUiState.ORDERBY_CREATE_ASC -> onOrderByChange(TodoUiState.ORDERBY_COMPLETE_DESC)
                        TodoUiState.ORDERBY_COMPLETE_DESC -> onOrderByChange(TodoUiState.ORDERBY_COMPLETE_ASC)
                        TodoUiState.ORDERBY_COMPLETE_ASC -> onOrderByChange(null)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun FilterMenuItemText(label: String, value: String) {
    Row {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onClick: () -> Unit,
    onStatusToggle: () -> Unit,
    onDelete: () -> Unit,
    dateFormat: SimpleDateFormat
) {
    val isCompleted = todo.isCompleted()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                onClickLabel = "编辑待办"
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCompleted) 1.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 已完成状态的左侧标识条
            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                        .width(4.dp)
                        .height(48.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF4CAF50).copy(alpha = 0.6f))
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = if (isCompleted) Spacing.Medium + 8.dp else Spacing.Medium,
                        top = Spacing.Medium,
                        end = Spacing.Medium,
                        bottom = Spacing.Medium
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 状态切换按钮
                        IconButton(
                            onClick = onStatusToggle,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isCompleted) {
                                    Icons.Filled.CheckCircle
                                } else {
                                    Icons.Outlined.CheckCircle
                                },
                                contentDescription = if (isCompleted) "标记为未完成" else "标记为已完成",
                                tint = if (isCompleted) {
                                    Color(0xFF4CAF50) // 绿色表示已完成
                                } else {
                                    MaterialTheme.colorScheme.outline
                                },
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(Spacing.Small))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = todo.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isCompleted) {
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                textDecoration = if (isCompleted) {
                                    TextDecoration.LineThrough
                                } else {
                                    null
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = if (isCompleted) FontWeight.Normal else FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(Spacing.ExtraSmall))

                            Text(
                                text = todo.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isCompleted) {
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "删除",
                            tint = if (isCompleted) {
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            },
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.Medium))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                    ) {
                        // 已完成状态使用去饱和的灰色标签
                        val typeColor = if (isCompleted) {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        } else {
                            getTypeColor(todo.type)
                        }
                        val priorityColor = if (isCompleted) {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        } else {
                            getPriorityColor(todo.priority)
                        }

                        FilterChip(
                            label = todo.getTypeName(),
                            color = typeColor,
                            icon = if (isCompleted) null else getTypeIcon(todo.type),
                            isCompleted = isCompleted
                        )
                        FilterChip(
                            label = todo.getPriorityName(),
                            color = priorityColor,
                            icon = null,
                            isCompleted = isCompleted
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (isCompleted) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            }
                        )
                        Text(
                            text = dateFormat.format(Date(todo.date)),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isCompleted) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    color: Color,
    icon: ImageVector?,
    isCompleted: Boolean = false
) {
    Surface(
        color = if (isCompleted) {
            color.copy(alpha = 0.08f)
        } else {
            color.copy(alpha = 0.12f)
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(28.dp),
        border = if (isCompleted) {
            androidx.compose.foundation.BorderStroke(
                width = 0.5.dp,
                color = color.copy(alpha = 0.3f)
            )
        } else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = color
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
        }
    }
}

@Composable
fun DeleteConfirmDialog(
    todo: Todo,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "确认删除",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = "确定要删除待办事项「${todo.title}」吗？此操作不可撤销。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "删除",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "取消",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun TodoLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FullScreenLoadingView(
            text = "加载中...",
            size = LoadingSize.LARGE
        )
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "重试",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = Spacing.Medium)
                )
            }
        }
    }
}

@Composable
fun EmptyContent(onAddClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            // 空状态图标
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(Spacing.Medium))

            Text(
                text = "暂无待办事项",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "开始规划你的一天，添加新的待办事项吧",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.Large))

            Button(
                onClick = onAddClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(Spacing.Small))
                Text(
                    text = "添加待办",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun getStatusName(status: Int?): String {
    return when (status) {
        null -> "全部"
        TodoUiState.STATUS_INCOMPLETE -> "未完成"
        TodoUiState.STATUS_COMPLETED -> "已完成"
        else -> "全部"
    }
}

private fun getTypeName(type: Int?): String {
    return when (type) {
        null -> "全部"
        Todo.TYPE_WORK -> "工作"
        Todo.TYPE_LIFE -> "生活"
        Todo.TYPE_ENTERTAINMENT -> "娱乐"
        else -> "全部"
    }
}

private fun getPriorityName(priority: Int?): String {
    return when (priority) {
        null -> "全部"
        Todo.PRIORITY_HIGH -> "高优先级"
        Todo.PRIORITY_NORMAL -> "普通"
        else -> "全部"
    }
}

private fun getOrderName(orderby: Int?): String {
    return when (orderby) {
        null -> "创建时间降序"
        TodoUiState.ORDERBY_CREATE_ASC -> "创建时间升序"
        TodoUiState.ORDERBY_COMPLETE_DESC -> "完成时间降序"
        TodoUiState.ORDERBY_COMPLETE_ASC -> "完成时间升序"
        else -> "创建时间降序"
    }
}

@Composable
private fun getTypeColor(type: Int): Color {
    return when (type) {
        Todo.TYPE_WORK -> Color(0xFF2196F3) // 蓝色
        Todo.TYPE_LIFE -> Color(0xFF4CAF50) // 绿色
        Todo.TYPE_ENTERTAINMENT -> Color(0xFFFF9800) // 橙色
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

@Composable
private fun getPriorityColor(priority: Int): Color {
    return when (priority) {
        Todo.PRIORITY_HIGH -> Color(0xFFF44336) // 红色
        Todo.PRIORITY_NORMAL -> Color(0xFF9E9E9E) // 灰色
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

private fun getTypeIcon(type: Int): ImageVector? {
    return when (type) {
        Todo.TYPE_WORK -> Icons.Outlined.DateRange
        Todo.TYPE_LIFE -> Icons.Outlined.CheckCircle
        Todo.TYPE_ENTERTAINMENT -> Icons.Filled.CheckCircle
        else -> null
    }
}
