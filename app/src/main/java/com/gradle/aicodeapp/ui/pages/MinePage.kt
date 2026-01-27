package com.gradle.aicodeapp.ui.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.ui.theme.Shapes
import com.gradle.aicodeapp.ui.theme.Spacing
import javax.inject.Inject

@Composable
fun MinePage(
    userManager: UserManager,
    onNavigateToCollect: () -> Unit = {},
    onNavigateToCoin: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToTodo: () -> Unit = {},
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
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
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
            Spacer(modifier = Modifier.height(Spacing.ExtraLarge))

            UserHeader(
                userManager = userManager
            )

            Spacer(modifier = Modifier.height(Spacing.Large))

            MenuSection(
                onNavigateToCollect = onNavigateToCollect,
                onNavigateToCoin = onNavigateToCoin,
                onNavigateToTodo = onNavigateToTodo,
                onNavigateToSettings = onNavigateToSettings
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))
        }
    }
}

@Composable
private fun UserHeader(
    userManager: UserManager
) {
    val username = userManager.getUsername()
    val nickname = userManager.getNickname()
    val icon = userManager.getIcon()
    val userId = userManager.getUserId()
    val isLoggedIn = userManager.isLoggedIn()

    val displayName = nickname ?: username ?: "未登录"
    val displayAvatar = icon

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.ScreenPadding),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.ExtraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (displayAvatar != null) {
                    Surface(
                        modifier = Modifier
                            .size(Spacing.IconHuge)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(displayAvatar)
                                .crossfade(true)
                                .build(),
                            contentDescription = "用户头像",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    Surface(
                        modifier = Modifier
                            .size(Spacing.IconHuge)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "用户头像",
                            modifier = Modifier
                                .size(Spacing.IconExtraLarge)
                                .padding(Spacing.Medium),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.Medium))

                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(Spacing.Small))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                ) {
                    if (isLoggedIn) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "已认证",
                            modifier = Modifier.size(Spacing.IconSmall),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "已登录",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (userId != null) {
                        Text(
                            text = "ID: ${maskUserId(userId)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
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
            .padding(horizontal = Spacing.ScreenPadding),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing.CornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Small)
        ) {
            MenuItem(
                icon = Icons.Default.Star,
                title = "我的积分",
                description = "查看积分详情",
                onClick = onNavigateToCoin
            )

            Divider(
                modifier = Modifier.padding(horizontal = Spacing.Medium),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )

            MenuItem(
                icon = Icons.Default.Favorite,
                title = "我的收藏",
                description = "查看收藏文章",
                onClick = onNavigateToCollect
            )

            Divider(
                modifier = Modifier.padding(horizontal = Spacing.Medium),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )

            MenuItem(
                icon = Icons.Default.CheckCircle,
                title = "待办事项",
                description = "管理待办任务",
                onClick = onNavigateToTodo
            )

            Divider(
                modifier = Modifier.padding(horizontal = Spacing.Medium),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )

            MenuItem(
                icon = Icons.Default.Settings,
                title = "设置",
                description = "应用设置",
                onClick = onNavigateToSettings
            )
        }
    }
}

@Composable
private fun MenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "backgroundColor"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.CornerRadiusMedium))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(Spacing.Medium)
            .scale(scale),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(Spacing.IconLarge),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(Spacing.IconMedium)
                    .padding(Spacing.Small),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(Spacing.Medium))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "进入",
            modifier = Modifier.size(Spacing.IconMedium),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}
