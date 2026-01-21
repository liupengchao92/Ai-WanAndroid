package com.gradle.aicodeapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.R
import com.gradle.aicodeapp.ui.theme.Spacing

data class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
)

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = Spacing.ElevationLow
    ) {
        val items = listOf(
            BottomNavItem("首页", R.drawable.ic_home),
            BottomNavItem("广场", R.drawable.ic_square),
            BottomNavItem("项目", R.drawable.ic_project),
            BottomNavItem("导航", R.drawable.ic_navigation),
            BottomNavItem("我的", R.drawable.ic_person)
        )

        items.forEachIndexed { index, item ->
            val interactionSource = remember { MutableInteractionSource() }

            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                interactionSource = interactionSource
            )
        }
    }
}
