package com.gradle.aicodeapp.utils

import android.util.Log

/**
 * 日志工具类，封装了Android的Log类，提供更方便的日志输出方法
 */
object LogUtils {

    private const val TAG = "AiCodeApp"
    private var isDebug = true // 是否开启调试模式

    /**
     * 设置是否开启调试模式
     * @param debug true: 开启调试模式，输出日志；false: 关闭调试模式，不输出日志
     */
    fun setDebug(debug: Boolean) {
        isDebug = debug
    }

    /**
     * 获取是否开启调试模式
     * @return true: 开启调试模式；false: 关闭调试模式
     */
    fun isDebug(): Boolean = isDebug

    /**
     * 输出Verbose级别的日志
     * @param tag 标签
     * @param message 消息
     */
    fun v(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.v(tag, message)
        }
    }

    /**
     * 输出Debug级别的日志
     * @param tag 标签
     * @param message 消息
     */
    fun d(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.d(tag, message)
        }
    }

    /**
     * 输出Info级别的日志
     * @param tag 标签
     * @param message 消息
     */
    fun i(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.i(tag, message)
        }
    }

    /**
     * 输出Warn级别的日志
     * @param tag 标签
     * @param message 消息
     */
    fun w(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.w(tag, message)
        }
    }

    /**
     * 输出Error级别的日志
     * @param tag 标签
     * @param message 消息
     */
    fun e(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.e(tag, message)
        }
    }

    /**
     * 输出Error级别的日志，包含异常信息
     * @param tag 标签
     * @param message 消息
     * @param throwable 异常
     */
    fun e(tag: String = TAG, message: String, throwable: Throwable) {
        if (isDebug) {
            Log.e(tag, message, throwable)
        }
    }
}
