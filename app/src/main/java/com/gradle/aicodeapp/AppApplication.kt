package com.gradle.aicodeapp

import android.app.Application
import com.gradle.aicodeapp.utils.AppContext
import com.gradle.aicodeapp.utils.LogUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化应用上下文
        AppContext.init(this)
        // 初始化日志工具
        LogUtils.setDebug(true) // 在开发环境中开启调试模式
        LogUtils.d("AppApplication", "Application initialized")
    }
}
