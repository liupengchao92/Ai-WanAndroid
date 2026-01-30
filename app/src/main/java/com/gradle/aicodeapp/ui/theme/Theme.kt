package com.gradle.aicodeapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * 主题配置
 * 基于Material 3设计系统，定义了应用的完整主题体系
 * 支持浅色/深色主题切换和动态色彩
 */

// 深色主题色彩方案
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    inversePrimary = InversePrimaryDark,
    surfaceTint = SurfaceTintDark
)

// 浅色主题色彩方案
private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    inversePrimary = InversePrimaryLight,
    surfaceTint = SurfaceTintLight
)

/**
 * 主题状态类，用于在Composition中传递主题相关信息
 */
data class ThemeState(
    val isDarkTheme: Boolean = false,
    val isDynamicColor: Boolean = false
)

/**
 * CompositionLocal用于在UI树中传递主题状态
 */
val LocalThemeState = staticCompositionLocalOf { ThemeState() }

/**
 * 应用主题
 * @param darkTheme 是否使用深色主题，默认跟随系统设置
 * @param dynamicColor 是否使用动态色彩，仅在Android 12+可用
 * @param content 主题内容
 */
@Composable
fun AiCodeAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // 提供主题状态给子组件
    CompositionLocalProvider(
        LocalThemeState provides ThemeState(isDarkTheme = darkTheme, isDynamicColor = dynamicColor)
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}

/**
 * 获取当前是否为深色主题的便捷方法
 */
@Composable
fun isDarkTheme(): Boolean {
    return LocalThemeState.current.isDarkTheme
}

/**
 * 主题切换动画辅助函数
 * 用于在主题切换时提供平滑的过渡效果
 */
@Composable
fun animateThemeColor(
    targetValue: Color,
    animationDuration: Int = 300
): Color {
    val animatedColor by animateColorAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = animationDuration),
        label = "theme_color_animation"
    )
    return animatedColor
}

/**
 * 主题使用规则
 * 1. 所有Composable函数都应包裹在AiCodeAppTheme中
 * 2. 使用MaterialTheme.colorScheme访问色彩
 * 3. 使用MaterialTheme.typography访问排版样式
 * 4. 使用MaterialTheme.shapes访问形状样式
 * 5. 避免硬编码颜色和字体样式，确保主题一致性
 * 6. 使用isDarkTheme()获取当前主题状态
 * 7. 使用animateThemeColor()为自定义组件添加主题切换动画
 */

/**
 * 主题评估指标
 * 1. 主题一致性：确保应用所有页面使用统一的主题
 * 2. 主题切换：验证浅色/深色主题切换是否正常
 * 3. 动态色彩：验证Android 12+设备上的动态色彩效果
 * 4. 可访问性：确保主题在不同对比度设置下正常显示
 * 5. 性能影响：确保主题切换不会影响应用性能
 * 6. 过渡动画：确保主题切换时有平滑的过渡效果
 */
