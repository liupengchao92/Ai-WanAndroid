package com.gradle.aicodeapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.gradle.aicodeapp.R

data class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
)

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
) {
    BottomAppBar {
        val items = listOf(
            BottomNavItem("首页", R.drawable.ic_home),
            BottomNavItem("广场", R.drawable.ic_square),
            BottomNavItem("项目", R.drawable.ic_project),
            BottomNavItem("导航", R.drawable.ic_navigation),
            BottomNavItem("我的", R.drawable.ic_person)
        )

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}
