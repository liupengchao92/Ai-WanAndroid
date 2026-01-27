package com.gradle.aicodeapp.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 响应式布局辅助函数
 */
object ResponsiveLayout {
    
    /**
     * 根据屏幕尺寸获取合适的水平外边距
     * 注意：此实现使用固定的外边距值，实际项目中可以根据需要扩展为基于屏幕宽度的动态计算
     */
    @Composable
    fun calculateHorizontalPadding(): Dp {
        // 暂时使用固定值，实际项目中可以根据屏幕宽度动态计算
        return Spacing.ScreenPadding
    }
    
    /**
     * 获取响应式内容内边距
     */
    @Composable
    fun responsiveContentPadding(): PaddingValues {
        val horizontalPadding = calculateHorizontalPadding()
        return PaddingValues(
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
        bottom: Dp = Spacing.Small
    ): Modifier {
        val horizontalPadding = calculateHorizontalPadding()
        return Modifier.padding(
            start = horizontalPadding,
            end = horizontalPadding,
            top = top,
            bottom = bottom
        )
    }
}