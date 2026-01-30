package com.gradle.aicodeapp.ui.pages

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.network.model.Todo
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.state.TodoUiState
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
            TopAppBar(
                title = { Text(text = "待办事项", style = MaterialTheme.typography.titleLarge) },
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加待办",
                    tint = MaterialTheme.colorScheme.onPrimary
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null && uiState.todos.isEmpty() -> {
                    val errorMessage = uiState.errorMessage ?: "未知错误"
                    ErrorContent(
                        message = errorMessage,
                        onRetry = { viewModel.loadTodoList() }
                    )
                }
                uiState.todos.isEmpty() -> {
                    EmptyContent()
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
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(Spacing.Medium),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
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

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "筛选"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("状态: ${getStatusName(filterStatus)}") },
                onClick = {
                    expanded = false
                    when (filterStatus) {
                        null -> onFilterStatusChange(TodoUiState.STATUS_INCOMPLETE)
                        TodoUiState.STATUS_INCOMPLETE -> onFilterStatusChange(TodoUiState.STATUS_COMPLETED)
                        TodoUiState.STATUS_COMPLETED -> onFilterStatusChange(null)
                    }
                }
            )
            DropdownMenuItem(
                text = { Text("类型: ${getTypeName(filterType)}") },
                onClick = {
                    expanded = false
                    when (filterType) {
                        null -> onFilterTypeChange(Todo.TYPE_WORK)
                        Todo.TYPE_WORK -> onFilterTypeChange(Todo.TYPE_LIFE)
                        Todo.TYPE_LIFE -> onFilterTypeChange(Todo.TYPE_ENTERTAINMENT)
                        Todo.TYPE_ENTERTAINMENT -> onFilterTypeChange(null)
                    }
                }
            )
            DropdownMenuItem(
                text = { Text("优先级: ${getPriorityName(filterPriority)}") },
                onClick = {
                    expanded = false
                    when (filterPriority) {
                        null -> onFilterPriorityChange(Todo.PRIORITY_HIGH)
                        Todo.PRIORITY_HIGH -> onFilterPriorityChange(Todo.PRIORITY_NORMAL)
                        Todo.PRIORITY_NORMAL -> onFilterPriorityChange(null)
                    }
                }
            )
            DropdownMenuItem(
                text = { Text("排序: ${getOrderName(orderby)}") },
                onClick = {
                    expanded = false
                    when (orderby) {
                        null -> onOrderByChange(TodoUiState.ORDERBY_CREATE_ASC)
                        TodoUiState.ORDERBY_CREATE_ASC -> onOrderByChange(TodoUiState.ORDERBY_COMPLETE_DESC)
                        TodoUiState.ORDERBY_COMPLETE_DESC -> onOrderByChange(TodoUiState.ORDERBY_COMPLETE_ASC)
                        TodoUiState.ORDERBY_COMPLETE_ASC -> onOrderByChange(null)
                    }
                }
            )
        }
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.ElevationLow),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.isCompleted()) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        shape = RoundedCornerShape(12.dp) // 使用标准的medium圆角
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
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onStatusToggle,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "切换状态",
                            tint = if (todo.isCompleted()) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outline
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(Spacing.Small))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = todo.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (todo.isCompleted()) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            textDecoration = if (todo.isCompleted()) {
                                TextDecoration.LineThrough
                            } else {
                                null
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(Spacing.ExtraSmall))

                        Text(
                            text = todo.content,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Small))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                ) {
                    val typeColor = getTypeColor(todo.type)
                    val priorityColor = getPriorityColor(todo.priority)
                    FilterChip(
                        label = todo.getTypeName(),
                        color = typeColor
                    )
                    FilterChip(
                        label = todo.getPriorityName(),
                        color = priorityColor
                    )
                }

                Text(
                    text = dateFormat.format(Date(todo.date)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FilterChip(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp)) // 使用标准的medium圆角
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = Spacing.Small, vertical = Spacing.ExtraSmall)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
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
        title = { Text(text = "确认删除", style = MaterialTheme.typography.titleMedium) },
        text = { Text(text = "确定要删除待办事项「${todo.title}」吗？", style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(8.dp) // 使用标准的small圆角
            ) {
                Text(text = "删除", style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "取消", style = MaterialTheme.typography.labelLarge)
            }
        }
    )
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(Spacing.Medium))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(8.dp) // 使用标准的small圆角
        ) {
            Text(text = "重试", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun EmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "暂无待办事项",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(Spacing.Small))
        Text(
            text = "点击右下角 + 添加新的待办",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
        Todo.PRIORITY_HIGH -> "高"
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
        Todo.TYPE_WORK -> MaterialTheme.colorScheme.primary
        Todo.TYPE_LIFE -> MaterialTheme.colorScheme.secondary
        Todo.TYPE_ENTERTAINMENT -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

@Composable
private fun getPriorityColor(priority: Int): Color {
    return when (priority) {
        Todo.PRIORITY_HIGH -> MaterialTheme.colorScheme.error
        Todo.PRIORITY_NORMAL -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}
