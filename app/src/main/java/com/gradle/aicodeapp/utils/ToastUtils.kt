package com.gradle.aicodeapp.utils

import android.content.Context
import android.widget.Toast

/**
 * Toast工具类，封装了Android的Toast类，提供更方便的Toast消息显示方法
 */
object ToastUtils {

    private var toast: Toast? = null

    /**
     * 显示短时长的Toast消息
     * @param context 上下文
     * @param message 消息内容
     */
    fun showShort(context: Context, message: String) {
        show(context, message, Toast.LENGTH_SHORT)
    }

    /**
     * 显示长时长的Toast消息
     * @param context 上下文
     * @param message 消息内容
     */
    fun showLong(context: Context, message: String) {
        show(context, message, Toast.LENGTH_LONG)
    }

    /**
     * 显示指定时长的Toast消息
     * @param context 上下文
     * @param message 消息内容
     * @param duration 时长，Toast.LENGTH_SHORT 或 Toast.LENGTH_LONG
     */
    private fun show(context: Context, message: String, duration: Int) {
        // 取消之前的Toast
        toast?.cancel()
        // 创建新的Toast
        toast = Toast.makeText(context, message, duration)
        // 显示Toast
        toast?.show()
    }

    /**
     * 取消当前显示的Toast
     */
    fun cancel() {
        toast?.cancel()
        toast = null
    }

    /**
     * 显示短时长的Toast消息（使用应用上下文）
     * @param message 消息内容
     */
    fun showShort(message: String) {
        val context = AppContext.get()
        if (context != null) {
            showShort(context, message)
        }
    }

    /**
     * 显示长时长的Toast消息（使用应用上下文）
     * @param message 消息内容
     */
    fun showLong(message: String) {
        val context = AppContext.get()
        if (context != null) {
            showLong(context, message)
        }
    }
}
