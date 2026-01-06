package com.gradle.aicodeapp.ui.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    BottomAppBar {
        val items = listOf(
            "首页",
            "广场",
            "项目",
            "导航",
            "我的"
        )

        items.forEachIndexed { index, title ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = { },
                label = { Text(title) }
            )
        }
    }
}
