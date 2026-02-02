package com.gradle.aicodeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * 状态栏透明化使用示例
 * 展示如何在不同场景下正确使用状态栏透明化功能
 */
object StatusBarExample {

    /**
     * 示例1: 基础使用 - 内容延伸到状态栏下方
     * 适用于大多数页面，自动为状态栏预留空间
     */
    @Composable
    fun BasicUsageExample() {
        StatusBarAwareContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "基础使用示例",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "此示例展示了如何使用 StatusBarAwareContainer 组件。" +
                           "内容会自动为状态栏预留空间，确保不会被状态栏遮挡。",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    /**
     * 示例2: 沉浸式布局 - 背景图延伸到状态栏
     * 适用于需要全屏背景图的场景，如引导页、个人主页等
     */
    @Composable
    fun ImmersiveExample() {
        ImmersiveContainer {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // 背景渐变
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                )

                // 内容（自动为导航栏预留空间）
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 状态栏占位符
                    StatusBarSpacer()

                    Text(
                        text = "沉浸式布局示例",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "背景延伸到状态栏区域",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    /**
     * 示例3: 全屏模式 - 内容延伸到状态栏和导航栏
     * 适用于视频播放、图片查看等全屏场景
     */
    @Composable
    fun FullScreenExample() {
        FullScreenContainer {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Text(
                    text = "全屏模式",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )

                // 提示文字
                Text(
                    text = "内容延伸到状态栏和导航栏区域",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp),
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    /**
     * 示例4: 带顶部栏和底部栏的完整布局
     * 适用于需要顶部栏和底部导航栏的页面
     */
    @Composable
    fun ScaffoldExample() {
        SystemAwareScaffold(
            topBar = {
                // 顶部栏（自动包含状态栏占位符）
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "顶部栏",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            bottomBar = {
                // 底部栏（自动包含导航栏占位符）
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "底部栏",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        ) {
            // 主要内容区域
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "主要内容区域",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "此示例展示了如何使用 SystemAwareScaffold 组件。" +
                           "顶部栏和底部栏会自动为系统栏预留空间。",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    /**
     * 示例5: 安全区域容器
     * 确保内容不会被刘海屏或圆角屏幕遮挡
     */
    @Composable
    fun SafeAreaExample() {
        SafeAreaContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "安全区域示例",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "此示例使用 SafeAreaContainer 组件，" +
                           "确保内容不会被刘海屏、圆角屏幕或系统栏遮挡。",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * 使用说明文档
 *
 * ## 状态栏透明化实现总结
 *
 * ### 1. 主题配置 (themes.xml)
 * - 设置 `android:statusBarColor` 为 `@android:color/transparent`
 * - 设置 `android:navigationBarColor` 为 `@android:color/transparent`
 * - 禁用 `windowTranslucentStatus` 和 `windowTranslucentNavigation`
 * - 启用 `windowLayoutInDisplayCutoutMode` 为 `shortEdges` 支持刘海屏
 *
 * ### 2. 动态配置 (Theme.kt)
 * - 使用 `ConfigureSystemBars(darkTheme)` 函数
 * - 自动根据主题设置状态栏文字颜色
 * - 浅色主题使用深色文字，深色主题使用浅色文字
 *
 * ### 3. 布局组件
 * - `StatusBarAwareContainer`: 自动为状态栏预留空间
 * - `SystemBarsAwareContainer`: 自动为状态栏和导航栏预留空间
 * - `ImmersiveContainer`: 内容延伸到状态栏，保留导航栏内边距
 * - `FullScreenContainer`: 全屏模式，内容延伸到所有系统栏
 * - `SafeAreaContainer`: 安全区域，防止内容被刘海屏遮挡
 * - `SystemAwareScaffold`: 完整的顶部栏和底部栏布局
 *
 * ### 4. 使用场景
 * - 普通页面: 使用 StatusBarAwareContainer
 * - 带背景图页面: 使用 ImmersiveContainer
 * - 视频/图片全屏: 使用 FullScreenContainer
 * - 需要顶部/底部栏: 使用 SystemAwareScaffold
 * - 刘海屏设备: 使用 SafeAreaContainer
 *
 * ### 5. 兼容性
 * - 支持 Android 6.0+ (API 23+)
 * - 状态栏文字颜色控制需要 Android 6.0+
 * - 导航栏图标颜色控制需要 Android 8.0+ (API 26+)
 * - 刘海屏支持需要 Android 9.0+ (API 28+)
 */
