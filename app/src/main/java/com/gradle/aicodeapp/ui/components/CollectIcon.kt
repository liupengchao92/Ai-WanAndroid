package com.gradle.aicodeapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CollectIcon(
    isCollected: Boolean,
    onCollectClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp
) {
    var isAnimating by remember { mutableStateOf(false) }
    var lastClickTime by remember { mutableStateOf(0L) }
    val interactionSource = remember { MutableInteractionSource() }

    val iconColor by animateColorAsState(
        targetValue = if (isCollected) Color.Red else Color.Gray,
        animationSpec = tween(durationMillis = 300),
        label = "iconColor"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isAnimating) 1.3f else 1.0f,
        animationSpec = tween(durationMillis = 150),
        label = "iconScale",
        finishedListener = {
            isAnimating = false
        }
    )

    fun handleCollectClick() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime < 500) {
            return
        }
        lastClickTime = currentTime
        isAnimating = true
        onCollectClick()
    }

    Icon(
        imageVector = if (isCollected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = if (isCollected) "取消收藏" else "收藏",
        tint = iconColor,
        modifier = modifier
            .size(iconSize)
            .scale(iconScale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                handleCollectClick()
            }
    )
}
