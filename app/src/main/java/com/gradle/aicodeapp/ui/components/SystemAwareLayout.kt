package com.gradle.aicodeapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.ui.theme.rememberStatusBarHeight
import com.gradle.aicodeapp.ui.theme.rememberNavigationBarHeight

/**
 * 状态栏高度感知的容器组件
 * 自动为内容添加状态栏高度的顶部内边距
 *
 * @param modifier 修饰符
 * @param content 内容
 */
@Composable
fun StatusBarAwareContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        content()
    }
}

/**
 * 系统栏高度感知的容器组件
 * 自动为内容添加状态栏和导航栏的内边距
 *
 * @param modifier 修饰符
 * @param content 内容
 */
@Composable
fun SystemBarsAwareContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        content()
    }
}

/**
 * 全屏内容容器（内容延伸到状态栏区域）
 * 适用于需要沉浸式体验的场景，如图片查看器、视频播放器等
 *
 * @param modifier 修饰符
 * @param content 内容
 */
@Composable
fun FullScreenContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        content()
    }
}

/**
 * 带状态栏占位符的顶部栏
 * 在顶部栏上方添加状态栏高度的空间
 *
 * @param modifier 修饰符
 * @param topBar 顶部栏内容
 */
@Composable
fun StatusBarSpacerTopBar(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 状态栏占位符
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )
        // 顶部栏内容
        topBar()
    }
}

/**
 * 带导航栏占位符的底部栏
 * 在底部栏下方添加导航栏高度的空间
 *
 * @param modifier 修饰符
 * @param bottomBar 底部栏内容
 */
@Composable
fun NavigationBarSpacerBottomBar(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 底部栏内容
        bottomBar()
        // 导航栏占位符
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        )
    }
}

/**
 * 键盘高度感知的容器
 * 当键盘弹出时自动调整底部内边距
 *
 * @param modifier 修饰符
 * @param content 内容
 */
@Composable
fun KeyboardAwareContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        content()
    }
}

/**
 * 完整的系统栏感知布局
 * 包含状态栏、导航栏和键盘的适配
 *
 * @param modifier 修饰符
 * @param topBar 顶部栏（可选）
 * @param bottomBar 底部栏（可选）
 * @param content 主要内容
 */
@Composable
fun SystemAwareScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // 顶部栏（包含状态栏占位符）
        topBar?.let {
            StatusBarSpacerTopBar(topBar = it)
        }

        // 主要内容区域
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            content()
        }

        // 底部栏（包含导航栏占位符）
        bottomBar?.let {
            NavigationBarSpacerBottomBar(bottomBar = it)
        }
    }
}

/**
 * 沉浸式内容布局（内容延伸到状态栏，但保留导航栏内边距）
 * 适用于需要背景图延伸到状态栏的场景
 *
 * @param modifier 修饰符
 * @param content 内容
 */
@Composable
fun ImmersiveContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        content()
    }
}

/**
 * 安全区域容器
 * 确保内容不会被系统栏（状态栏、导航栏、刘海屏）遮挡
 *
 * @param modifier 修饰符
 * @param content 内容
 */
@Composable
fun SafeAreaContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        content()
    }
}

/**
 * 状态栏高度占位符
 * 用于在需要手动控制布局时为状态栏预留空间
 */
@Composable
fun StatusBarSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    )
}

/**
 * 导航栏高度占位符
 * 用于在需要手动控制布局时为导航栏预留空间
 */
@Composable
fun NavigationBarSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    )
}

/**
 * 固定高度的状态栏占位符
 * 使用实际状态栏高度
 */
@Composable
fun FixedStatusBarSpacer() {
    val statusBarHeight = rememberStatusBarHeight()
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(statusBarHeight.value.dp)
    )
}

/**
 * 固定高度的导航栏占位符
 * 使用实际导航栏高度
 */
@Composable
fun FixedNavigationBarSpacer() {
    val navigationBarHeight = rememberNavigationBarHeight()
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(navigationBarHeight.value.dp)
    )
}
