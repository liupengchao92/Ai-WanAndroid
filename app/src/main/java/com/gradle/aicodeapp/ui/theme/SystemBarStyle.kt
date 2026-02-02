package com.gradle.aicodeapp.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * 系统栏样式配置
 * 提供状态栏和导航栏的透明化及样式控制
 */
object SystemBarStyle {

    /**
     * 配置窗口为Edge-to-Edge模式
     * 使应用内容可以延伸到状态栏和导航栏区域
     */
    fun configureEdgeToEdge(window: Window) {
        // 禁用系统默认的fitsSystemWindows行为
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 设置状态栏为透明
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        // 设置导航栏为透明
        window.navigationBarColor = android.graphics.Color.TRANSPARENT

        // 针对Android 10+ (API 29+) 设置导航栏为手势导航模式下的透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        // 针对Android 11+ (API 30+) 设置状态栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }
    }

    /**
     * 设置状态栏文字和图标颜色
     * @param activity 当前Activity
     * @param lightStatusBar true表示使用深色文字（适用于浅色背景），false表示使用浅色文字（适用于深色背景）
     */
    fun setStatusBarTextColor(activity: Activity, lightStatusBar: Boolean) {
        val window = activity.window
        val decorView = window.decorView

        val windowInsetsController = WindowCompat.getInsetsController(window, decorView)
        windowInsetsController.isAppearanceLightStatusBars = lightStatusBar
    }

    /**
     * 设置导航栏图标颜色
     * @param activity 当前Activity
     * @param lightNavigationBar true表示使用深色图标，false表示使用浅色图标
     */
    fun setNavigationBarIconColor(activity: Activity, lightNavigationBar: Boolean) {
        val window = activity.window
        val decorView = window.decorView

        val windowInsetsController = WindowCompat.getInsetsController(window, decorView)
        windowInsetsController.isAppearanceLightNavigationBars = lightNavigationBar
    }

    /**
     * 根据背景颜色自动设置状态栏文字颜色
     * @param activity 当前Activity
     * @param backgroundColor 背景颜色
     */
    fun autoSetStatusBarColor(activity: Activity, backgroundColor: Color) {
        val isLightBackground = isLightColor(backgroundColor)
        setStatusBarTextColor(activity, isLightBackground)
    }

    /**
     * 判断颜色是否为浅色
     */
    private fun isLightColor(color: Color): Boolean {
        val red = color.red * 255
        val green = color.green * 255
        val blue = color.blue * 255
        val luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255
        return luminance > 0.5
    }
}

/**
 * Compose函数：自动配置系统栏样式
 * 根据当前主题自动设置状态栏和导航栏的文字/图标颜色
 *
 * @param darkTheme 是否为深色主题
 */
@Composable
fun ConfigureSystemBars(darkTheme: Boolean) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // 配置Edge-to-Edge模式
            SystemBarStyle.configureEdgeToEdge(window)

            // 设置状态栏文字颜色：浅色主题使用深色文字，深色主题使用浅色文字
            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            windowInsetsController.isAppearanceLightStatusBars = !darkTheme

            // 设置导航栏图标颜色
            windowInsetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }
}

/**
 * Compose函数：获取状态栏高度
 * 用于在布局中为状态栏预留空间
 */
@Composable
fun rememberStatusBarHeight(): androidx.compose.runtime.State<Int> {
    val view = LocalView.current
    return androidx.compose.runtime.produceState(initialValue = 0) {
        val windowInsets = ViewCompat.getRootWindowInsets(view)
        val statusBarInsets = windowInsets?.getInsets(WindowInsetsCompat.Type.statusBars())
        value = statusBarInsets?.top ?: 0
    }
}

/**
 * Compose函数：获取导航栏高度
 * 用于在布局中为导航栏预留空间
 */
@Composable
fun rememberNavigationBarHeight(): androidx.compose.runtime.State<Int> {
    val view = LocalView.current
    return androidx.compose.runtime.produceState(initialValue = 0) {
        val windowInsets = ViewCompat.getRootWindowInsets(view)
        val navigationBarInsets = windowInsets?.getInsets(WindowInsetsCompat.Type.navigationBars())
        value = navigationBarInsets?.bottom ?: 0
    }
}

/**
 * 系统栏样式配置数据类
 */
data class SystemBarColors(
    val statusBarColor: Color = Color.Transparent,
    val navigationBarColor: Color = Color.Transparent,
    val isLightStatusBar: Boolean = true,
    val isLightNavigationBar: Boolean = true
)

/**
 * 系统栏样式默认值
 */
object SystemBarDefaults {
    fun defaultColors(darkTheme: Boolean) = SystemBarColors(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent,
        isLightStatusBar = !darkTheme,
        isLightNavigationBar = !darkTheme
    )
}
