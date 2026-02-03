package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import com.gradle.aicodeapp.ui.components.AppTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.R
import com.gradle.aicodeapp.network.model.Message
import com.gradle.aicodeapp.ui.theme.Primary
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.MessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagePage(
    viewModel: MessageViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadUnreadCount()
    }

    LaunchedEffect(uiState.isLoginRequired) {
        if (uiState.isLoginRequired) {
            onNavigateToLogin()
        }
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = stringResource(R.string.messages),
                onNavigationClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Primary
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        if (uiState.unreadMessages.isEmpty()) {
                            viewModel.loadUnreadMessages()
                        }
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(stringResource(R.string.unread_messages))
                            if (uiState.unreadCount > 0) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (uiState.unreadCount > 99) "99+" else uiState.unreadCount.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        if (uiState.readMessages.isEmpty()) {
                            viewModel.loadReadMessages()
                        }
                    },
                    text = { Text(stringResource(R.string.read_messages)) }
                )
            }

            when (selectedTab) {
                0 -> UnreadMessageList(
                    messages = uiState.unreadMessages,
                    isLoading = uiState.isLoading,
                    isRefreshing = uiState.isRefreshing,
                    isLoadingMore = uiState.isLoadingMore,
                    hasMoreData = uiState.hasMoreUnreadData,
                    errorMessage = uiState.errorMessage,
                    onRefresh = { viewModel.refreshUnreadMessages() },
                    onLoadMore = { viewModel.loadMoreUnreadMessages() },
                    onClearError = { viewModel.clearError() }
                )
                1 -> ReadMessageList(
                    messages = uiState.readMessages,
                    isLoading = uiState.isLoading,
                    isRefreshing = uiState.isRefreshing,
                    isLoadingMore = uiState.isLoadingMore,
                    hasMoreData = uiState.hasMoreReadData,
                    errorMessage = uiState.errorMessage,
                    onRefresh = { viewModel.refreshReadMessages() },
                    onLoadMore = { viewModel.loadMoreReadMessages() },
                    onClearError = { viewModel.clearError() }
                )
            }
        }
    }
}

@Composable
private fun UnreadMessageList(
    messages: List<Message>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    errorMessage: String?,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onClearError: () -> Unit
) {
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            isLoading && messages.isEmpty() -> {
                LoadingContent()
            }
            messages.isEmpty() && !isLoading -> {
                EmptyContent(
                    icon = painterResource(R.drawable.ic_notifications),
                    title = stringResource(R.string.no_unread_messages),
                    subtitle = stringResource(R.string.no_unread_messages_subtitle)
                )
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = Spacing.Small)
                ) {
                    items(messages) { message ->
                        MessageItem(
                            message = message,
                            isUnread = true
                        )
                    }

                    if (isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Spacing.Medium),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        if (hasMoreData && !isLoadingMore && listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == messages.size - 1) {
            onLoadMore()
        }
    }
}

@Composable
private fun ReadMessageList(
    messages: List<Message>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    errorMessage: String?,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onClearError: () -> Unit
) {
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            isLoading && messages.isEmpty() -> {
                LoadingContent()
            }
            messages.isEmpty() && !isLoading -> {
                EmptyContent(
                    icon = painterResource(R.drawable.ic_mark_email_read),
                    title = stringResource(R.string.no_read_messages),
                    subtitle = stringResource(R.string.no_read_messages_subtitle)
                )
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = Spacing.Small)
                ) {
                    items(messages) { message ->
                        MessageItem(
                            message = message,
                            isUnread = false
                        )
                    }

                    if (isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Spacing.Medium),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        if (hasMoreData && !isLoadingMore && listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == messages.size - 1) {
            onLoadMore()
        }
    }
}

@Composable
private fun MessageItem(
    message: Message,
    isUnread: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Medium, vertical = Spacing.Small),
        shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnread) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isUnread) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = if (isUnread) painterResource(R.drawable.ic_email) else painterResource(R.drawable.ic_mark_email_read),
                            contentDescription = null,
                            tint = if (isUnread) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(Spacing.Medium))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = message.fromUser,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = message.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (isUnread) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(8.dp)
                    ) {}
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Small))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(Spacing.Small))

            Text(
                text = message.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(Spacing.Small))

            Text(
                text = message.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyContent(
    icon: Painter,
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.ExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(80.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.Large))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Spacing.Small))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
