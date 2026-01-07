package com.gradle.aicodeapp.utils

import android.content.Context

/**
 * 应用上下文工具类，提供获取应用上下文的方法
 */
object AppContext {

    private var context: Context? = null

    /**
     * 初始化应用上下文
     * @param appContext 应用上下文
     */
    fun init(appContext: Context) {
        context = appContext.applicationContext
    }

    /**
     * 获取应用上下文
     * @return 应用上下文
     */
    fun get(): Context? = context

    /**
     * 检查应用上下文是否已初始化
     * @return true: 已初始化；false: 未初始化
     */
    fun isInitialized(): Boolean = context != null
}
