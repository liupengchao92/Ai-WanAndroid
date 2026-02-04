package com.gradle.aicodeapp.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 响应式布局系统规范
 * 基于Material 3设计系统，定义了应用的完整响应式适配策略
 */

/**
 * 屏幕尺寸断点
 */
enum class ScreenSize {
    SMALL,    // < 600dp
    MEDIUM,   // 600dp - 840dp
    LARGE     // > 840dp
}

/**
 * 响应式布局辅助函数
 */
object ResponsiveLayout {

    /**
     * 获取当前屏幕尺寸
     */
    @Composable
    fun getScreenSize(): ScreenSize {
        val configuration = LocalConfiguration.current
        return when {
            configuration.screenWidthDp < 600 -> ScreenSize.SMALL
            configuration.screenWidthDp < 840 -> ScreenSize.MEDIUM
            else -> ScreenSize.LARGE
        }
    }

    /**
     * 根据屏幕尺寸获取合适的水平外边距
     */
    @Composable
    fun calculateHorizontalPadding(): Dp {
        return when (getScreenSize()) {
            ScreenSize.SMALL -> 16.dp
            ScreenSize.MEDIUM -> 24.dp
            ScreenSize.LARGE -> 32.dp
        }
    }

    /**
     * 根据屏幕尺寸获取合适的内容宽度
     */
    @Composable
    fun calculateContentWidth(): Dp {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val horizontalPadding = calculateHorizontalPadding()
        return screenWidth - (horizontalPadding * 2)
    }

    /**
     * 获取响应式内容内边距
     */
    @Composable
    fun responsiveContentPadding(top: Dp = 0.dp): PaddingValues {
        val horizontalPadding = calculateHorizontalPadding()
        return PaddingValues(
            top = top,
            start = horizontalPadding,
            end = horizontalPadding,
            bottom = Spacing.ExtraLarge
        )
    }

    /**
     * 获取响应式修饰符，包含水平外边距
     */
    @Composable
    fun responsiveHorizontalPadding(): Modifier {
        val horizontalPadding = calculateHorizontalPadding()
        return Modifier.padding(
            start = horizontalPadding,
            end = horizontalPadding
        )
    }

    /**
     * 获取响应式修饰符，包含顶部和水平外边距
     */
    @Composable
    fun responsiveTopAndHorizontalPadding(topPadding: Dp = Spacing.Small): Modifier {
        val horizontalPadding = calculateHorizontalPadding()
        return Modifier.padding(
            start = horizontalPadding,
            end = horizontalPadding,
            top = topPadding
        )
    }

    /**
     * 获取响应式修饰符，包含底部和水平外边距
     */
    @Composable
    fun responsiveBottomAndHorizontalPadding(bottomPadding: Dp = Spacing.Small): Modifier {
        val horizontalPadding = calculateHorizontalPadding()
        return Modifier.padding(
            start = horizontalPadding,
            end = horizontalPadding,
            bottom = bottomPadding
        )
    }

    /**
     * 获取响应式修饰符，包含所有方向的外边距
     */
    @Composable
    fun responsivePadding(
        top: Dp = Spacing.Small,
        bottom: Dp = Spacing.Small,
    ): Modifier {
        val horizontalPadding = calculateHorizontalPadding()
        return Modifier.padding(
            start = horizontalPadding,
            end = horizontalPadding,
            top = top,
            bottom = bottom
        )
    }

    /**
     * 获取响应式修饰符，包含安全区域内边距
     */
    @Composable
    fun responsiveSafePadding(): Modifier {
        val windowInsets = WindowInsets.safeDrawing
        return Modifier.windowInsetsPadding(windowInsets)
    }

    /**
     * 获取响应式修饰符，包含显示切口安全区域
     */
    @Composable
    fun responsiveCutoutPadding(): Modifier {
        val windowInsets = WindowInsets.displayCutout
        return Modifier.windowInsetsPadding(windowInsets)
    }

    /**
     * 获取响应式字体大小
     */
    @Composable
    fun responsiveFontSize(baseSize: Float, scaleFactor: Float = 1f): TextUnit {
        val screenSize = getScreenSize()
        val scale = when (screenSize) {
            ScreenSize.SMALL -> 0.9f
            ScreenSize.MEDIUM -> 1.0f
            ScreenSize.LARGE -> 1.1f
        }
        return (baseSize * scale * scaleFactor).sp
    }

    /**
     * 获取响应式文本样式
     */
    @Composable
    fun responsiveTextStyle(baseStyle: TextStyle): TextStyle {
        val screenSize = getScreenSize()
        val scale = when (screenSize) {
            ScreenSize.SMALL -> 0.9f
            ScreenSize.MEDIUM -> 1.0f
            ScreenSize.LARGE -> 1.1f
        }
        return baseStyle.copy(
            fontSize = (baseStyle.fontSize.value * scale).sp,
            lineHeight = (baseStyle.lineHeight.value * scale).sp
        )
    }

    /**
     * 获取响应式间距
     */
    @Composable
    fun responsiveSpacing(baseSpacing: Dp): Dp {
        val screenSize = getScreenSize()
        val scale = when (screenSize) {
            ScreenSize.SMALL -> 0.8f
            ScreenSize.MEDIUM -> 1.0f
            ScreenSize.LARGE -> 1.2f
        }
        return (baseSpacing.value * scale).dp
    }

    /**
     * 获取响应式网格列数
     */
    @Composable
    fun calculateGridColumns(): Int {
        return when (getScreenSize()) {
            ScreenSize.SMALL -> 1
            ScreenSize.MEDIUM -> 2
            ScreenSize.LARGE -> 3
        }
    }

    /**
     * 获取响应式卡片宽度
     */
    @Composable
    fun calculateCardWidth(columnCount: Int = calculateGridColumns()): Dp {
        val contentWidth = calculateContentWidth()
        val spacing = Spacing.Medium
        val totalSpacing = spacing * (columnCount - 1)
        return (contentWidth - totalSpacing) / columnCount
    }
}

/**
 * 响应式适配策略
 * 1. 小屏幕（< 600dp）：单列布局，简化导航，使用较小的间距和字体
 * 2. 中屏幕（600dp - 840dp）：适当增加布局复杂度，考虑双列布局，使用标准间距和字体
 * 3. 大屏幕（> 840dp）：优化空间利用，支持多列布局，使用较大的间距和字体
 * 4. 关键文本：确保关键文本在所有屏幕尺寸下都清晰可见
 * 5. 触摸目标：确保触摸目标在所有屏幕尺寸下都至少为48dp
 */

/**
 * 响应式评估指标
 * 1. 布局一致性：在不同屏幕尺寸下保持品牌一致性
 * 2. 内容完整性：确保所有内容在各种屏幕尺寸下都能完整显示
 * 3. 空间利用率：在大屏幕上合理利用额外空间
 * 4. 可读性：确保文本在所有屏幕尺寸下都清晰可读
 * 5. 交互体验：确保触摸目标在所有屏幕尺寸下都易于点击
 */
