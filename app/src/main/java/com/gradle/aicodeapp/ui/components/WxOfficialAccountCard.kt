package com.gradle.aicodeapp.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.network.model.WxOfficialAccount
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.theme.ResponsiveLayout
import kotlin.math.abs

/**
 * 公众号列表卡片组件
 * 展示公众号列表，支持水平滑动，分两行显示
 */
@Composable
fun WxOfficialAccountCard(
    accounts: List<WxOfficialAccount>,
    isLoading: Boolean,
    errorMessage: String?,
    onAccountClick: (WxOfficialAccount) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            //.then(ResponsiveLayout.responsiveHorizontalPadding())
            .padding(vertical = Spacing.Small),
        shape = RoundedCornerShape(Spacing.CornerRadiusCard),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Spacing.ElevationLow
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.Medium)
        ) {
            // 标题栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "公众号",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else if (errorMessage != null) {
                    IconButton(
                        onClick = onRetryClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "重试",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.Medium))

            when {
                isLoading && accounts.isEmpty() -> {
                    // 加载中状态
                    WxAccountsLoadingPlaceholder()
                }

                errorMessage != null && accounts.isEmpty() -> {
                    // 错误状态
                    WxAccountsErrorPlaceholder(
                        errorMessage = errorMessage,
                        onRetryClick = onRetryClick
                    )
                }

                accounts.isEmpty() -> {
                    // 空数据状态
                    WxAccountsEmptyPlaceholder()
                }

                else -> {
                    // 正常显示公众号列表
                    WxOfficialAccountGrid(
                        accounts = accounts,
                        onAccountClick = onAccountClick
                    )
                }
            }
        }
    }
}

/**
 * 公众号网格布局
 * 分两行显示，每行水平滑动
 */
@Composable
private fun WxOfficialAccountGrid(
    accounts: List<WxOfficialAccount>,
    onAccountClick: (WxOfficialAccount) -> Unit
) {
    // 将列表分成两行
    val midPoint = (accounts.size + 1) / 2
    val firstRow = accounts.take(midPoint)
    val secondRow = accounts.drop(midPoint)

    Column {
        // 第一行
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = Spacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            items(firstRow, key = { it.id }) { account ->
                WxOfficialAccountItem(
                    account = account,
                    onClick = { onAccountClick(account) }
                )
            }
        }

        if (secondRow.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Spacing.Medium))

            // 第二行
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = Spacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                items(secondRow, key = { it.id }) { account ->
                    WxOfficialAccountItem(
                        account = account,
                        onClick = { onAccountClick(account) }
                    )
                }
            }
        }
    }
}

/**
 * 公众号单项组件
 */
@Composable
private fun WxOfficialAccountItem(
    account: WxOfficialAccount,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
        ),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        } else {
            Color.Transparent
        },
        label = "backgroundColor"
    )

    Column(
        modifier = Modifier
            .width(64.dp)
            .scale(scale)
            .clip(RoundedCornerShape(Spacing.CornerRadiusMedium))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = Spacing.Small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 圆形头像，显示名称首字符（字母自动大写）
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    color = generateAvatarColor(account.name)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getFirstCharUppercase(account.name),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(Spacing.Small))

        // 公众号名称
        Text(
            text = account.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 加载中占位符
 */
@Composable
private fun WxAccountsLoadingPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(Spacing.Small))
        Text(
            text = "加载中...",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 错误状态占位符
 */
@Composable
private fun WxAccountsErrorPlaceholder(
    errorMessage: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Large)
            .clickable(onClick = onRetryClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(Spacing.Small))
        Text(
            text = "加载失败，点击重试",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

/**
 * 空数据占位符
 */
@Composable
private fun WxAccountsEmptyPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "暂无公众号数据",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 获取首字符（字母自动转为大写）
 * @param name 公众号名称
 * @return 首字符，如果是字母则转为大写
 */
private fun getFirstCharUppercase(name: String): String {
    if (name.isEmpty()) return "公"
    val firstChar = name.first()
    return if (firstChar.isLetter()) {
        firstChar.uppercaseChar().toString()
    } else {
        firstChar.toString()
    }
}

/**
 * 根据名称生成头像颜色
 */
private fun generateAvatarColor(name: String): Color {
    val colors = listOf(
        Color(0xFF2196F3), // Blue
        Color(0xFF4CAF50), // Green
        Color(0xFFFF9800), // Orange
        Color(0xFF9C27B0), // Purple
        Color(0xFFF44336), // Red
        Color(0xFF00BCD4), // Cyan
        Color(0xFFFF5722), // Deep Orange
        Color(0xFF3F51B5), // Indigo
        Color(0xFF009688), // Teal
        Color(0xFFE91E63), // Pink
        Color(0xFF795548), // Brown
        Color(0xFF607D8B)  // Blue Grey
    )

    val hash = abs(name.hashCode())
    return colors[hash % colors.size]
}