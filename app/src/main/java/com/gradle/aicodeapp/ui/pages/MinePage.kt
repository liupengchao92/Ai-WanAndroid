package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.ui.theme.Shapes
import com.gradle.aicodeapp.ui.theme.Spacing
import javax.inject.Inject

@Composable
fun MinePage(
    userManager: UserManager,
    onLogout: () -> Unit,
    onNavigateToCollect: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val scrollState = rememberScrollState()

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
            onNavigateToCollect = onNavigateToCollect
        )

        Spacer(modifier = Modifier.weight(1f))

        LogoutButton(
            onLogout = onLogout
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))
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
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.ElevationLow),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                    color = MaterialTheme.colorScheme.primaryContainer
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
                    color = MaterialTheme.colorScheme.primaryContainer
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
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
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
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (userId != null) {
                    Text(
                        text = "ID: ${maskUserId(userId)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
    onNavigateToCollect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.ScreenPadding),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.ElevationLow),
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
                icon = Icons.Default.Favorite,
                title = "我的收藏",
                onClick = onNavigateToCollect
            )

            Divider(
                modifier = Modifier.padding(horizontal = Spacing.Medium),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            MenuItem(
                icon = Icons.Default.List,
                title = "我的文章",
                onClick = {}
            )

            Divider(
                modifier = Modifier.padding(horizontal = Spacing.Medium),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            MenuItem(
                icon = Icons.Default.Settings,
                title = "设置",
                onClick = {}
            )
        }
    }
}

@Composable
private fun MenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    isPressed = true
                    onClick()
                }
            )
            .padding(Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(Spacing.IconLarge),
            tint = if (isPressed) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )

        Spacer(modifier = Modifier.width(Spacing.Medium))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isPressed) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "进入",
            modifier = Modifier.size(Spacing.IconMedium),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LogoutButton(
    onLogout: () -> Unit
) {
    Button(
        onClick = onLogout,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.ScreenPadding)
            .height(Spacing.ButtonHeight),
        shape = Shapes.Medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "退出登录",
            modifier = Modifier.size(Spacing.IconMedium)
        )
        Spacer(modifier = Modifier.width(Spacing.Small))
        Text(
            text = "退出登录",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
