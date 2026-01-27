package com.gradle.aicodeapp.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 色彩系统规范
 * 基于Material 3设计系统，定义了应用的完整色彩体系
 */

// 主色系 - 蓝色系，作为应用的主要品牌色
// 应用场景：主要操作按钮、关键链接、重要信息强调
val Primary = Color(0xFF1E88E5)           // 主品牌色
val PrimaryLight = Color(0xFF64B5F6)      // 主色浅色变体
val PrimaryDark = Color(0xFF0D47A1)       // 主色深色变体
val OnPrimary = Color(0xFFFFFFFF)         // 主色上的文字颜色
val PrimaryContainer = Color(0xFFE3F2FD)  // 主色容器背景
val OnPrimaryContainer = Color(0xFF0D47A1) // 主色容器上的文字颜色

// 辅助色系 - 青色系，用于次要操作和信息
// 应用场景：次要操作、标签、分类信息
val Secondary = Color(0xFF03A9F4)         // 辅助色
val SecondaryLight = Color(0xFF4FC3F7)    // 辅助色浅色变体
val SecondaryDark = Color(0xFF01579B)     // 辅助色深色变体
val OnSecondary = Color(0xFFFFFFFF)       // 辅助色上的文字颜色
val SecondaryContainer = Color(0xFFE1F5FE) // 辅助色容器背景
val OnSecondaryContainer = Color(0xFF01579B) // 辅助色容器上的文字颜色

// 第三色系 - 紫色系，用于特殊功能和强调
// 应用场景：特殊功能、创新元素、品牌亮点
val Tertiary = Color(0xFF9C27B0)          // 第三色
val TertiaryLight = Color(0xFFCE93D8)     // 第三色浅色变体
val TertiaryDark = Color(0xFF4A148C)      // 第三色深色变体
val OnTertiary = Color(0xFFFFFFFF)        // 第三色上的文字颜色
val TertiaryContainer = Color(0xFFF3E5F5) // 第三色容器背景
val OnTertiaryContainer = Color(0xFF4A148C) // 第三色容器上的文字颜色

// 状态色系
// 应用场景：根据语义使用对应的状态色
val Error = Color(0xFFE53935)             // 错误状态
val ErrorLight = Color(0xFFEF5350)        // 错误状态浅色变体
val ErrorDark = Color(0xFFB71C1C)         // 错误状态深色变体
val OnError = Color(0xFFFFFFFF)           // 错误状态上的文字颜色
val ErrorContainer = Color(0xFFFFEBEE)    // 错误状态容器背景
val OnErrorContainer = Color(0xFFB71C1C)  // 错误状态容器上的文字颜色

val Success = Color(0xFF43A047)           // 成功状态
val SuccessLight = Color(0xFF66BB6A)      // 成功状态浅色变体
val SuccessDark = Color(0xFF1B5E20)       // 成功状态深色变体
val SuccessContainer = Color(0xFFE8F5E9)  // 成功状态容器背景
val OnSuccessContainer = Color(0xFF1B5E20) // 成功状态容器上的文字颜色

val Warning = Color(0xFFFF9800)           // 警告状态
val WarningLight = Color(0xFFFFB74D)      // 警告状态浅色变体
val WarningDark = Color(0xFFE65100)       // 警告状态深色变体
val WarningContainer = Color(0xFFFFF3E0)  // 警告状态容器背景
val OnWarningContainer = Color(0xFFE65100) // 警告状态容器上的文字颜色

val Info = Color(0xFF1E88E5)              // 信息状态
val InfoLight = Color(0xFF64B5F6)         // 信息状态浅色变体
val InfoDark = Color(0xFF0D47A1)          // 信息状态深色变体
val InfoContainer = Color(0xFFE3F2FD)     // 信息状态容器背景
val OnInfoContainer = Color(0xFF0D47A1)   // 信息状态容器上的文字颜色

// 背景色系
// 应用场景：页面背景、卡片背景等
val Background = Color(0xFFFAFAFA)        // 主背景色
val BackgroundLight = Color(0xFFFFFFFF)   // 背景色浅色变体
val BackgroundDark = Color(0xFFF5F5F5)    // 背景色深色变体
val OnBackground = Color(0xFF212121)      // 背景上的文字颜色

val Surface = Color(0xFFFFFFFF)           // 表面色
val SurfaceLight = Color(0xFFFFFFFF)      // 表面色浅色变体
val SurfaceDark = Color(0xFFF5F5F5)       // 表面色深色变体
val OnSurface = Color(0xFF212121)         // 表面上的文字颜色
val SurfaceVariant = Color(0xFFF5F5F5)    // 表面变体色
val OnSurfaceVariant = Color(0xFF757575)  // 表面变体上的文字颜色

// 轮廓和分隔线
// 应用场景：边框、分隔线、输入框轮廓等
val Outline = Color(0xFFE0E0E0)           // 轮廓色
val OutlineVariant = Color(0xFFEEEEEE)    // 轮廓变体色
val DividerColor = Color(0xFFE0E0E0)      // 分隔线颜色

// 反转色系 - 用于深色主题
// 应用场景：深色主题下的强调元素
val InverseSurface = Color(0xFF212121)    // 反转表面色
val InverseOnSurface = Color(0xFFFAFAFA)  // 反转表面上的文字颜色
val InversePrimary = Color(0xFF90CAF9)    // 反转主色

// 表面色层级
// 应用场景：不同层级的表面元素，创建视觉深度
val SurfaceDim = Color(0xFFE0E0E0)        // 暗淡表面色
val SurfaceBright = Color(0xFFFFFFFF)     // 明亮表面色
val SurfaceContainerLowest = Color(0xFFFFFFFF) // 最低层级容器色
val SurfaceContainerLow = Color(0xFFF5F5F5)    // 低层级容器色
val SurfaceContainer = Color(0xFFEEEEEE)       // 中层级容器色
val SurfaceContainerHigh = Color(0xFFE0E0E0)   // 高层级容器色
val SurfaceContainerHighest = Color(0xFFE0E0E0) // 最高层级容器色

// 特殊用途颜色
val SurfaceTint = Color(0xFF1E88E5)       // 表面着色

// 文本颜色层级
// 应用场景：不同重要性的文本内容
val TextPrimary = Color(0xFF212121)       // 主要文本颜色
val TextSecondary = Color(0xFF757575)     // 次要文本颜色
val TextHint = Color(0xFFBDBDBD)          // 提示文本颜色
val TextDisabled = Color(0xFFBDBDBD)      // 禁用文本颜色
val TextLink = Color(0xFF1E88E5)          // 链接文本颜色

// 卡片颜色
// 应用场景：卡片背景
val CardBackground = Color(0xFFFFFFFF)     // 卡片背景色
val CardBackgroundVariant = Color(0xFFF5F5F5) // 卡片背景变体色

// 浮动按钮颜色
// 应用场景：浮动操作按钮
val FabBackground = Color(0xFF1E88E5)      // 浮动按钮背景色
val FabContent = Color(0xFFFFFFFF)         // 浮动按钮内容色

// 阴影颜色
// 应用场景：卡片、按钮等元素的阴影
val ShadowColor = Color(0x20000000)        // 阴影颜色
val ElevatedShadowColor = Color(0x40000000) // 高海拔阴影颜色

// 保留旧颜色以确保兼容性
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

/**
 * 色彩应用规则
 * 1. 主色（Primary）：用于主要操作按钮、关键链接和重要信息强调
 * 2. 辅助色（Secondary）：用于次要操作、标签和分类信息
 * 3. 第三色（Tertiary）：用于特殊功能、创新元素和品牌亮点
 * 4. 状态色：严格按照语义使用（错误-红色、成功-绿色、警告-橙色、信息-蓝色）
 * 5. 文本色：根据背景色自动调整对比度，确保可读性
 * 6. 背景色：使用不同层级的背景色创建视觉深度
 * 7. 轮廓色：用于边框、分隔线等元素，区分不同区域
 */

/**
 * 色彩评估指标
 * 1. 色彩对比度：确保文本与背景对比度符合WCAG 2.1 AA标准
 * 2. 色彩一致性：同一类型元素在不同页面使用相同色彩
 * 3. 色彩和谐度：通过色彩理论确保整体视觉和谐
 * 4. 色彩可访问性：考虑色盲用户的体验，使用多种视觉提示
 */