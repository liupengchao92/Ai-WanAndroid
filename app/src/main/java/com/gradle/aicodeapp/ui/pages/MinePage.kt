package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.CachePolicy
import com.gradle.aicodeapp.R
import com.gradle.aicodeapp.data.UserManager
import androidx.compose.foundation.Image
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.ui.theme.CustomShapes
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.theme.ResponsiveLayout
import com.gradle.aicodeapp.ui.theme.Primary
import com.gradle.aicodeapp.ui.theme.PrimaryContainer
import com.gradle.aicodeapp.ui.theme.Secondary
import com.gradle.aicodeapp.ui.theme.SecondaryContainer
import com.gradle.aicodeapp.ui.theme.Tertiary
import com.gradle.aicodeapp.ui.theme.TertiaryContainer
import com.gradle.aicodeapp.ui.theme.Success
import com.gradle.aicodeapp.ui.theme.SuccessContainer
import com.gradle.aicodeapp.ui.theme.Warning
import com.gradle.aicodeapp.ui.theme.WarningContainer
import com.gradle.aicodeapp.ui.viewmodel.MessageViewModel
import javax.inject.Inject

@Composable
fun MinePage(
    userManager: UserManager,
    messageViewModel: MessageViewModel = hiltViewModel(),
    onNavigateToCollect: () -> Unit = {},
    onNavigateToCoin: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToTodo: () -> Unit = {},
    onNavigateToMessage: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.Large))

            UserHeaderCard(
                userManager = userManager,
                messageViewModel = messageViewModel,
                onNavigateToMessage = onNavigateToMessage
            )

            Spacer(modifier = Modifier.height(Spacing.Large))

          /*  QuickStatsSection(
                userManager = userManager
            )

            Spacer(modifier = Modifier.height(Spacing.Large))*/

            MenuSection(
                onNavigateToCollect = onNavigateToCollect,
                onNavigateToCoin = onNavigateToCoin,
                onNavigateToTodo = onNavigateToTodo,
                onNavigateToSettings = onNavigateToSettings
            )

            Spacer(modifier = Modifier.height(Spacing.ExtraLarge))
        }
    }
}

@Composable
private fun UserHeaderCard(
    userManager: UserManager,
    messageViewModel: MessageViewModel,
    onNavigateToMessage: () -> Unit = {}
) {
    val username = userManager.getUsername()
    val nickname = userManager.getNickname()
    val icon = userManager.getIcon()
    val userId = userManager.getUserId()
    val isLoggedIn = userManager.isLoggedIn()

    val displayName = nickname ?: username ?: stringResource(R.string.click_to_login)
    val displayAvatar = icon

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            messageViewModel.loadUnreadCount()
        }
    }

    val messageUiState by messageViewModel.uiState.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(ResponsiveLayout.responsiveHorizontalPadding())
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(Spacing.CornerRadiusExtraLarge),
                spotColor = Primary.copy(alpha = 0.15f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing.CornerRadiusExtraLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // 顶部渐变装饰条
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Primary.copy(alpha = 0.8f),
                                Secondary.copy(alpha = 0.6f),
                                Tertiary.copy(alpha = 0.4f)
                            ),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
            )

            // 装饰性圆形
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .offset(x = (-30).dp, y = (-30).dp)
                    .alpha(0.1f)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .offset(x = 280.dp, y = 20.dp)
                    .alpha(0.08f)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
            )

            // 消息图标
            if (isLoggedIn) {
                MessageIconWithBadge(
                    onClick = onNavigateToMessage,
                    unreadCount = messageUiState.unreadCount,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Spacing.Medium)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.ExtraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // 头像容器
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AvatarImage(
                        avatarUrl = displayAvatar,
                        size = 100.dp
                    )

                    // 编辑图标
                    if (isLoggedIn) {
                        Surface(
                            modifier = Modifier
                                .size(32.dp)
                                .offset(x = (-4).dp, y = (-4).dp),
                            shape = CircleShape,
                            color = Primary,
                            shadowElevation = 4.dp
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(8.dp),
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.Medium))

                // 用户名
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(Spacing.Small))

                // 用户状态行
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                ) {
                    if (isLoggedIn) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SuccessContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 4.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Success
                                )
                                Text(
                                    text = stringResource(R.string.logged_in),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Success
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = stringResource(R.string.not_logged_in),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 4.dp
                                )
                            )
                        }
                    }

                    if (userId != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = stringResource(R.string.user_id_format, maskUserId(userId)),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 4.dp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageIconWithBadge(
    onClick: () -> Unit,
    unreadCount: Int,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            shadowElevation = 4.dp,
            modifier = Modifier.size(44.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.messages),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // 红色提示徽章
        if (unreadCount > 0) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickStatsSection(
    userManager: UserManager
) {
    val isLoggedIn = userManager.isLoggedIn()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(ResponsiveLayout.responsiveHorizontalPadding()),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        StatCard(
            icon = Icons.Default.Star,
            value = "--",
            label = stringResource(R.string.points),
            iconBackground = WarningContainer,
            iconTint = Warning,
            modifier = Modifier.weight(1f)
        )

        StatCard(
            icon = Icons.Default.Favorite,
            value = "--",
            label = stringResource(R.string.favorites),
            iconBackground = SecondaryContainer,
            iconTint = Secondary,
            modifier = Modifier.weight(1f)
        )

        StatCard(
            icon = Icons.Default.CheckCircle,
            value = "--",
            label = stringResource(R.string.todos),
            iconBackground = SuccessContainer,
            iconTint = Success,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = iconBackground
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
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

private fun maskUserId(userId: Int): String {
    val userIdStr = userId.toString()
    return when {
        userIdStr.length <= 2 -> userIdStr
        userIdStr.length <= 4 -> "${userIdStr.first()}***${userIdStr.last()}"
        else -> "${userIdStr.take(2)}***${userIdStr.takeLast(2)}"
    }
}

@Composable
private fun MenuSection(
    onNavigateToCollect: () -> Unit,
    onNavigateToCoin: () -> Unit,
    onNavigateToTodo: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(ResponsiveLayout.responsiveHorizontalPadding())
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(Spacing.CornerRadiusExtraLarge),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing.CornerRadiusExtraLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.Small)
        ) {
            MenuItem(
                icon = Icons.Default.Star,
                title = stringResource(R.string.my_points),
                description = stringResource(R.string.view_points_details),
                iconBackground = WarningContainer,
                iconTint = Warning,
                onClick = onNavigateToCoin,
                isFirst = true
            )

            MenuDivider()

            MenuItem(
                icon = Icons.Default.Favorite,
                title = stringResource(R.string.my_favorites),
                description = stringResource(R.string.view_favorite_articles),
                iconBackground = SecondaryContainer,
                iconTint = Secondary,
                onClick = onNavigateToCollect
            )

            MenuDivider()

            MenuItem(
                icon = Icons.Default.CheckCircle,
                title = stringResource(R.string.todo_list),
                description = stringResource(R.string.manage_todo_tasks),
                iconBackground = SuccessContainer,
                iconTint = Success,
                onClick = onNavigateToTodo
            )

            MenuDivider()

            MenuItem(
                icon = Icons.Default.Settings,
                title = stringResource(R.string.settings),
                description = stringResource(R.string.app_settings),
                iconBackground = PrimaryContainer,
                iconTint = Primary,
                onClick = onNavigateToSettings,
                isLast = true
            )
        }
    }
}

@Composable
private fun MenuDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = Spacing.Large),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
        thickness = 0.5.dp
    )
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    title: String,
    description: String,
    iconBackground: Color,
    iconTint: Color,
    onClick: () -> Unit,
    isFirst: Boolean = false,
    isLast: Boolean = false
) {
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
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 150),
        label = "background"
    )

    val paddingTop = if (isFirst) Spacing.Medium else Spacing.Small
    val paddingBottom = if (isLast) Spacing.Medium else Spacing.Small

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .padding(horizontal = Spacing.Medium)
            .clip(RoundedCornerShape(Spacing.CornerRadiusMedium))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(
                start = Spacing.Medium,
                end = Spacing.Medium,
                top = paddingTop,
                bottom = paddingBottom
            ),
        verticalAlignment = Alignment.CenterVertically
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
                    contentDescription = title,
                    modifier = Modifier.size(22.dp),
                    tint = iconTint
                )
            }
        }

        Spacer(modifier = Modifier.width(Spacing.Medium))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.width(Spacing.Small))

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.enter),
                modifier = Modifier
                    .size(28.dp)
                    .padding(4.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * 头像图片组件
 * 支持网络图片和本地默认头像回退
 *
 * @param avatarUrl 用户头像URL，为null时显示默认头像
 * @param size 头像尺寸
 * @param contentDescription 内容描述
 */
@Composable
private fun AvatarImage(
    avatarUrl: String?,
    size: androidx.compose.ui.unit.Dp,
    contentDescription: String = stringResource(R.string.user_avatar)
) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                spotColor = Primary.copy(alpha = 0.3f)
            ),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 0.dp
    ) {
        if (avatarUrl != null) {
            // 使用 Coil 加载网络图片，支持缓存
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(avatarUrl)
                    .crossfade(true)
                    // 内存缓存策略
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    // 磁盘缓存策略
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .placeholder(R.drawable.ic_avatar)
                    .error(R.drawable.ic_avatar)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // 显示本地默认头像
            Image(
                painter = painterResource(id = R.drawable.ic_avatar),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
