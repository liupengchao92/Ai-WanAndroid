package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.gradle.aicodeapp.ui.components.AppTopAppBar
import com.gradle.aicodeapp.ui.components.FullScreenLoadingView
import com.gradle.aicodeapp.ui.components.InlineLoadingView
import com.gradle.aicodeapp.ui.components.LoadingSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.ui.state.CoinUiState
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.theme.ResponsiveLayout
import com.gradle.aicodeapp.ui.theme.Primary
import com.gradle.aicodeapp.ui.theme.PrimaryContainer
import com.gradle.aicodeapp.ui.theme.Secondary
import com.gradle.aicodeapp.ui.theme.SecondaryContainer
import com.gradle.aicodeapp.ui.theme.Tertiary
import com.gradle.aicodeapp.ui.theme.TertiaryContainer
import com.gradle.aicodeapp.ui.theme.Warning
import com.gradle.aicodeapp.ui.theme.WarningContainer
import com.gradle.aicodeapp.ui.theme.Success
import com.gradle.aicodeapp.ui.theme.SuccessContainer
import com.gradle.aicodeapp.ui.viewmodel.CoinViewModel
import kotlin.math.min

@Composable
private fun getScreenSize(): Dp {
    val configuration = LocalConfiguration.current
    return minOf(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
}

@Composable
private fun isSmallScreen(): Boolean {
    return getScreenSize() < 600.dp
}

@OptIn(ExperimentalMaterial3Api::class)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        when {
            uiState.isLoginRequired -> LoginRequiredView(onNavigateToLogin = onNavigateToLogin)
            uiState.isLoading && uiState.coinRankList.isEmpty() -> LoadingView()
            uiState.errorMessage != null && uiState.coinRankList.isEmpty() -> CoinErrorView(
                errorMessage = uiState.errorMessage ?: "未知错误",
                onRetry = { viewModel.refreshData() }
            )
            else -> CoinContentView(
                uiState = uiState,
                onLoadMore = { viewModel.loadMoreRank() },
                paddingValues = paddingValues,
                onNavigateBack = onNavigateBack
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
            AppTopAppBar(
                title = "积分排行榜",
                onNavigationClick = onNavigateBack
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = ResponsiveLayout.calculateHorizontalPadding(),
                    vertical = Spacing.Medium
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
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Spacing.Medium),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                        thickness = 0.5.dp
                    )
                }

                item {
                    AnimatedVisibility(
                        visible = uiState.isLoadingMore,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        InlineLoadingView(
                            size = LoadingSize.SMALL
                        )
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = !uiState.hasMoreRankData && uiState.coinRankList.isNotEmpty(),
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.Medium),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "已经到底啦 ~",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(Spacing.CornerRadiusExtraLarge),
                spotColor = Primary.copy(alpha = 0.15f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(Spacing.CornerRadiusExtraLarge)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 顶部渐变装饰
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Primary.copy(alpha = 0.9f),
                                Secondary.copy(alpha = 0.7f),
                                Tertiary.copy(alpha = 0.5f)
                            ),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
            )

            // 装饰性圆形
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset(x = (-20).dp, y = (-20).dp)
                    .alpha(0.1f)
                    .background(Color.White, CircleShape)
            )

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .offset(x = 300.dp, y = 30.dp)
                    .alpha(0.08f)
                    .background(Color.White, CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.ExtraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                if (isLoading && coinUserInfo == null) {
                    InlineLoadingView(
                        size = LoadingSize.MEDIUM
                    )
                } else {
                    // 积分图标
                    Surface(
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = CircleShape,
                                spotColor = Warning.copy(alpha = 0.3f)
                            ),
                        shape = CircleShape,
                        color = WarningContainer
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "积分",
                                modifier = Modifier.size(40.dp),
                                tint = Warning
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Spacing.Medium))

                    // 用户名
                    Text(
                        text = coinUserInfo?.username ?: "未登录",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(Spacing.Large))

                    // 积分信息行
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
                    ) {
                        CoinStatCard(
                            icon = Icons.Default.Star,
                            label = "总积分",
                            value = coinUserInfo?.coinCount?.toString() ?: "0",
                            iconBackground = WarningContainer,
                            iconTint = Warning,
                            modifier = Modifier.weight(1f)
                        )

                        CoinStatCard(
                            icon = Icons.Default.CheckCircle,
                            label = "当前排名",
                            value = "#${coinUserInfo?.rank ?: "-"}",
                            iconBackground = SuccessContainer,
                            iconTint = Success,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CoinStatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    iconBackground: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
                spotColor = iconTint.copy(alpha = 0.1f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconBackground
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = iconTint
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Small))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RankListHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(Spacing.CornerRadiusMedium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.Medium, vertical = Spacing.Small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "排名",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.12f)
            )

            Text(
                text = "用户",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.53f)
            )

            Text(
                text = "积分",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.35f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun RankListItem(
    rank: com.gradle.aicodeapp.network.model.CoinRank
) {
    val rankNumber = rank.rank.toIntOrNull() ?: 0
    val smallScreen = isSmallScreen()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 150),
        label = "background"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(Spacing.CornerRadiusMedium))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { }
            ),
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
                modifier = Modifier.weight(if (smallScreen) 0.55f else 0.53f)
            )

            Spacer(modifier = Modifier.width(if (smallScreen) 4.dp else Spacing.Small))

            CoinCount(
                coinCount = rank.coinCount,
                rankNumber = rankNumber,
                modifier = Modifier.weight(if (smallScreen) 0.3f else 0.35f)
            )
        }
    }
}

@Composable
private fun RankNumber(rankNumber: Int) {
    val isTopThree = rankNumber <= 3

    Box(
        modifier = Modifier
            .size(40.dp)
            .shadow(
                elevation = if (isTopThree) 6.dp else 2.dp,
                shape = CircleShape,
                spotColor = when (rankNumber) {
                    1 -> Tertiary.copy(alpha = 0.4f)
                    2 -> Secondary.copy(alpha = 0.4f)
                    3 -> MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                    else -> Color.Transparent
                }
            )
            .background(
                when (rankNumber) {
                    1 -> Tertiary
                    2 -> Secondary
                    3 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.surfaceVariant
                },
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isTopThree) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "排名 $rankNumber",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
        } else {
            Text(
                text = rankNumber.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
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
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (nickname != null) {
            Text(
                text = username,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CoinCount(
    coinCount: Int,
    rankNumber: Int,
    modifier: Modifier = Modifier
) {
    val isTopThree = rankNumber <= 3

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isTopThree) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 4.dp),
                tint = when (rankNumber) {
                    1 -> Tertiary
                    2 -> Secondary
                    else -> MaterialTheme.colorScheme.error
                }
            )
        }

        Text(
            text = coinCount.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = when (rankNumber) {
                1 -> Tertiary
                2 -> Secondary
                3 -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.primary
            }
        )
    }
}

@Composable
private fun LoadingView() {
    FullScreenLoadingView(
        text = "加载积分数据中...",
        size = LoadingSize.LARGE
    )
}

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
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "错误",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "重试",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(Spacing.Small))
                Text(
                    text = "重新加载",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
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
            verticalArrangement = Arrangement.spacedBy(Spacing.Large),
            modifier = Modifier.padding(Spacing.ExtraLarge)
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = PrimaryContainer,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "积分",
                        modifier = Modifier.size(60.dp),
                        tint = Primary
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Text(
                    text = "需要登录",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "登录后查看您的积分和排行榜",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = onNavigateToLogin,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                modifier = Modifier.padding(top = Spacing.Medium)
            ) {
                Text(
                    text = "立即登录",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
