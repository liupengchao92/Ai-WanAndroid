package com.gradle.aicodeapp.ui.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.gradle.aicodeapp.data.preferences.SettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * 语言管理器
 * 负责管理应用语言设置和提供语言切换功能
 */
object LanguageManager {
    private val _currentLanguage = MutableStateFlow(SettingsDataStore.LANGUAGE_ZH)
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    /**
     * 更新当前语言并通知所有监听者
     */
    fun setLanguage(languageCode: String) {
        _currentLanguage.value = languageCode
    }

    /**
     * 获取当前语言代码
     */
    fun getCurrentLanguage(): String = _currentLanguage.value

    /**
     * 获取对应语言的 Locale
     */
    fun getLocale(languageCode: String): Locale {
        return when (languageCode) {
            SettingsDataStore.LANGUAGE_ZH -> Locale.SIMPLIFIED_CHINESE
            SettingsDataStore.LANGUAGE_EN -> Locale.ENGLISH
            SettingsDataStore.LANGUAGE_JA -> Locale.JAPANESE
            else -> Locale.SIMPLIFIED_CHINESE
        }
    }

    /**
     * 更新应用资源配置（仅用于 Activity 级别的资源更新）
     */
    fun updateResources(context: Context, languageCode: String) {
        val locale = getLocale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * 更新 Activity 的配置（推荐的方式）
     */
    fun updateActivityConfiguration(activity: Activity, languageCode: String) {
        val locale = getLocale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)
        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
    }
}

/**
 * CompositionLocal 用于在 Compose 树中传递语言状态
 */
val LocalLanguageState = staticCompositionLocalOf { SettingsDataStore.LANGUAGE_ZH }

/**
 * 语言包装器
 * 包装内容并提供语言状态，当语言变化时触发重组
 *
 * 注意：不替换 LocalContext，以避免破坏 Hilt 的依赖注入
 */
@Composable
fun LanguageWrapper(
    languageCode: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // 使用 remember 来跟踪语言变化
    val languageState = remember(languageCode) {
        LanguageManager.setLanguage(languageCode)
        languageCode
    }

    // 创建一个新的 Configuration 来触发重组
    // 这是 Android 推荐的方式，不会破坏协程作用域
    val configuration = Configuration(LocalConfiguration.current).apply {
        setLocale(LanguageManager.getLocale(languageCode))
    }

    // 更新资源
    LaunchedEffect(languageCode) {
        LanguageManager.updateResources(context, languageCode)
    }

    // 使用 CompositionLocalProvider 提供新的 Configuration
    // 这会触发使用 stringResource 的组件重组
    CompositionLocalProvider(
        LocalLanguageState provides languageState,
        LocalConfiguration provides configuration
    ) {
        content()
    }
}

/**
 * 获取当前语言的便捷方法
 */
@Composable
fun currentLanguage(): String {
    return LocalLanguageState.current
}

/**
 * 语言切换动画状态
 */
sealed class LanguageChangeState {
    data object Idle : LanguageChangeState()
    data object Changing : LanguageChangeState()
    data class Success(val language: String) : LanguageChangeState()
    data class Error(val message: String) : LanguageChangeState()
}
