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
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.ui.theme.Shapes
import com.gradle.aicodeapp.ui.theme.Spacing

@Composable
fun MinePage(
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

        UserHeader()

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
private fun UserHeader() {
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

            Spacer(modifier = Modifier.height(Spacing.Medium))

            Text(
                text = "用户中心",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
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
                icon = Icons.Default.Person,
                title = "我的文章",
                onClick = {}
            )

            Divider(
                modifier = Modifier.padding(horizontal = Spacing.Medium),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            MenuItem(
                icon = Icons.Default.Person,
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(Spacing.IconMedium),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(Spacing.Medium))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
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
