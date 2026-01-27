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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.R
import com.gradle.aicodeapp.ui.state.CoinUiState
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.CoinViewModel

@Composable
private fun getScreenSize(): Dp {
    val configuration = LocalConfiguration.current
    return minOf(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
}

@Composable
private fun isSmallScreen(): Boolean {
    return getScreenSize() < 600.dp
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CoinPage(
    viewModel: CoinViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onNavigateToLogin: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    if (uiState.isLoginRequired) {
        LoginRequiredView(onNavigateToLogin = onNavigateToLogin)
    } else if (uiState.isLoading && uiState.coinRankList.isEmpty()) {
        LoadingView()
    } else if (uiState.errorMessage != null && uiState.coinRankList.isEmpty()) {
        CoinErrorView(
            errorMessage = uiState.errorMessage ?: "未知错误",
            onRetry = { viewModel.refreshData() }
        )
    } else {
        CoinContentView(
            uiState = uiState,
            onLoadMore = { viewModel.loadMoreRank() },
            paddingValues = paddingValues,
            onNavigateBack = onNavigateBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun CoinContentView(
    uiState: CoinUiState,
    onLoadMore: () -> Unit,
    paddingValues: PaddingValues,
    onNavigateBack: () -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null &&
            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 3 &&
            !uiState.isLoadingMore &&
            uiState.hasMoreRankData
        }
    }.value

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("积分排行榜") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { contentPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = Spacing.ScreenPadding,
                vertical = Spacing.Small
            )
        ) {
            item {
                UserInfoCard(
                    coinUserInfo = uiState.coinUserInfo,
                    isLoading = uiState.isLoading
                )
                Spacer(modifier = Modifier.height(Spacing.Large))
            }

            item {
                RankListHeader()
                Spacer(modifier = Modifier.height(Spacing.Small))
            }

            items(
                items = uiState.coinRankList,
                key = { it.userId }
            ) { rank ->
                RankListItem(rank = rank)
                Divider(
                    modifier = Modifier.padding(horizontal = Spacing.Medium),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            item {
                AnimatedVisibility(visible = uiState.isLoadingMore) {
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

            item {
                AnimatedVisibility(visible = !uiState.hasMoreRankData && uiState.coinRankList.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.Medium),

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
        }
    }
}

@Composable
private fun UserInfoCard(
    coinUserInfo: com.gradle.aicodeapp.network.model.CoinUserInfo?,
    isLoading: Boolean
) {
    val smallScreen = isSmallScreen()
    val paddingValue = if (smallScreen) Spacing.Medium else Spacing.Large
    val spacingValue = if (smallScreen) Spacing.Medium else Spacing.Large

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.ElevationLow),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(Spacing.Medium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .padding(paddingValue)
        ) {
            if (isLoading && coinUserInfo == null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "我的积分",
                        style = if (smallScreen) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(spacingValue))

                    Text(
                        text = coinUserInfo?.username ?: "未登录",
                        style = if (smallScreen) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(Spacing.Medium))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CoinInfoItem(
                            label = "总积分",
                            value = coinUserInfo?.coinCount?.toString() ?: "0",
                            color = MaterialTheme.colorScheme.primary
                        )

                        CoinInfoItem(
                            label = "当前排名",
                            value = "#${coinUserInfo?.rank ?: "-"}",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CoinInfoItem(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(modifier = Modifier.height(Spacing.ExtraSmall))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun RankListHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Medium),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "排名",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.15f)
        )

        Text(
            text = "用户",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.5f)
        )

        Text(
            text = "积分",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.35f),
            textAlign = TextAlign.End
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun RankListItem(
    rank: com.gradle.aicodeapp.network.model.CoinRank
) {
    val rankNumber = rank.rank.toIntOrNull() ?: 0
    val smallScreen = isSmallScreen()

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (smallScreen) Spacing.Small else Spacing.Medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RankNumber(rankNumber = rankNumber)

            Spacer(modifier = Modifier.width(if (smallScreen) 4.dp else Spacing.Small))

            UserInfo(
                username = rank.username,
                nickname = rank.nickname,
                modifier = Modifier.weight(if (smallScreen) 0.55f else 0.5f)
            )

            Spacer(modifier = Modifier.width(if (smallScreen) 4.dp else Spacing.Small))

            CoinCount(
                coinCount = rank.coinCount,
                modifier = Modifier.weight(if (smallScreen) 0.3f else 0.35f)
            )
        }
    }
}

@Composable
private fun RankNumber(rankNumber: Int) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(
                when (rankNumber) {
                    1 -> ComposeColor(0xFFFFD700) // 金色
                    2 -> ComposeColor(0xFFC0C0C0) // 银色
                    3 -> ComposeColor(0xFFCD7F32) // 铜色
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
            .shadow(
                elevation = when (rankNumber) {
                    1, 2, 3 -> 4.dp
                    else -> 2.dp
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (rankNumber <= 3) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "奖杯",
                modifier = Modifier.size(22.dp),
                tint = ComposeColor.White
            )
        } else {
            Text(
                text = rankNumber.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun UserInfo(
    username: String,
    nickname: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = nickname ?: username,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (nickname != null) {
            Text(
                text = username,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CoinCount(
    coinCount: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = coinCount.toString(),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
        textAlign = TextAlign.End
    )
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(Spacing.Medium),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp
                )
            }

            Text(
                text = "加载积分数据...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CoinErrorView(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.Large),
            modifier = Modifier.padding(Spacing.ExtraLarge)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f))
                    .padding(Spacing.Medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "错误",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            androidx.compose.material3.Button(
                    onClick = onRetry,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "重试",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(Spacing.Small))
                Text("重试")
            }
        }
    }
}

@Composable
private fun LoginRequiredView(
    onNavigateToLogin: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
            modifier = Modifier.padding(Spacing.ExtraLarge)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "积分",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "需要登录",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "请先登录后查看积分信息",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            androidx.compose.material3.Button(
                onClick = onNavigateToLogin,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("去登录")
            }
        }
    }
}
