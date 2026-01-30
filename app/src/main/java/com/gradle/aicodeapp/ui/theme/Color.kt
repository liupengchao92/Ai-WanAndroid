package com.gradle.aicodeapp.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 色彩系统规范
 * 基于Material 3设计系统，定义了应用的完整色彩体系
 * 支持浅色和深色主题
 */

// ==================== 浅色主题颜色 ====================

// 主色系 - 蓝色系
val PrimaryLight = Color(0xFF1E88E5)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFE3F2FD)
val OnPrimaryContainerLight = Color(0xFF0D47A1)

// 辅助色系 - 青色系
val SecondaryLight = Color(0xFF03A9F4)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFE1F5FE)
val OnSecondaryContainerLight = Color(0xFF01579B)

// 第三色系 - 紫色系
val TertiaryLight = Color(0xFF9C27B0)
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFFF3E5F5)
val OnTertiaryContainerLight = Color(0xFF4A148C)

// 错误状态颜色
val ErrorLight = Color(0xFFE53935)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFEBEE)
val OnErrorContainerLight = Color(0xFFB71C1C)

// 背景颜色
val BackgroundLight = Color(0xFFFAFAFA)
val OnBackgroundLight = Color(0xFF212121)

// 表面颜色
val SurfaceLight = Color(0xFFFFFFFF)
val OnSurfaceLight = Color(0xFF212121)
val SurfaceVariantLight = Color(0xFFF5F5F5)
val OnSurfaceVariantLight = Color(0xFF757575)

// 轮廓颜色
val OutlineLight = Color(0xFFE0E0E0)
val OutlineVariantLight = Color(0xFFEEEEEE)

// 反转颜色（用于特殊场景）
val InverseSurfaceLight = Color(0xFF212121)
val InverseOnSurfaceLight = Color(0xFFFAFAFA)
val InversePrimaryLight = Color(0xFF90CAF9)

// 表面着色
val SurfaceTintLight = Color(0xFF1E88E5)

// ==================== 深色主题颜色 ====================

// 主色系 - 蓝色系（深色主题使用更柔和的色调）
val PrimaryDark = Color(0xFF90CAF9)
val OnPrimaryDark = Color(0xFF0D47A1)
val PrimaryContainerDark = Color(0xFF1565C0)
val OnPrimaryContainerDark = Color(0xFFE3F2FD)

// 辅助色系 - 青色系
val SecondaryDark = Color(0xFF4FC3F7)
val OnSecondaryDark = Color(0xFF01579B)
val SecondaryContainerDark = Color(0xFF0277BD)
val OnSecondaryContainerDark = Color(0xFFE1F5FE)

// 第三色系 - 紫色系
val TertiaryDark = Color(0xFFCE93D8)
val OnTertiaryDark = Color(0xFF4A148C)
val TertiaryContainerDark = Color(0xFF6A1B9A)
val OnTertiaryContainerDark = Color(0xFFF3E5F5)

// 错误状态颜色
val ErrorDark = Color(0xFFEF5350)
val OnErrorDark = Color(0xFFB71C1C)
val ErrorContainerDark = Color(0xFFC62828)
val OnErrorContainerDark = Color(0xFFFFEBEE)

// 背景颜色
val BackgroundDark = Color(0xFF121212)
val OnBackgroundDark = Color(0xFFE0E0E0)

// 表面颜色
val SurfaceDark = Color(0xFF1E1E1E)
val OnSurfaceDark = Color(0xFFE0E0E0)
val SurfaceVariantDark = Color(0xFF2C2C2C)
val OnSurfaceVariantDark = Color(0xFFBDBDBD)

// 轮廓颜色
val OutlineDark = Color(0xFF424242)
val OutlineVariantDark = Color(0xFF373737)

// 反转颜色（用于特殊场景）
val InverseSurfaceDark = Color(0xFFE0E0E0)
val InverseOnSurfaceDark = Color(0xFF121212)
val InversePrimaryDark = Color(0xFF1E88E5)

// 表面着色
val SurfaceTintDark = Color(0xFF90CAF9)

// ==================== 扩展颜色（兼容旧代码） ====================

// 主色系扩展
val Primary = PrimaryLight
val PrimaryLightExt = Color(0xFF64B5F6)
val PrimaryDarkExt = Color(0xFF0D47A1)
val OnPrimary = OnPrimaryLight
val PrimaryContainer = PrimaryContainerLight
val OnPrimaryContainer = OnPrimaryContainerLight

// 辅助色系扩展
val Secondary = SecondaryLight
val SecondaryLightExt = Color(0xFF4FC3F7)
val SecondaryDarkExt = Color(0xFF01579B)
val OnSecondary = OnSecondaryLight
val SecondaryContainer = SecondaryContainerLight
val OnSecondaryContainer = OnSecondaryContainerLight

// 第三色系扩展
val Tertiary = TertiaryLight
val TertiaryLightExt = Color(0xFFCE93D8)
val TertiaryDarkExt = Color(0xFF4A148C)
val OnTertiary = OnTertiaryLight
val TertiaryContainer = TertiaryContainerLight
val OnTertiaryContainer = OnTertiaryContainerLight

// 状态色扩展
val Error = ErrorLight
val ErrorLightExt = Color(0xFFEF5350)
val ErrorDarkExt = Color(0xFFB71C1C)
val OnError = OnErrorLight
val ErrorContainer = ErrorContainerLight
val OnErrorContainer = OnErrorContainerLight

val Success = Color(0xFF43A047)
val SuccessLightExt = Color(0xFF66BB6A)
val SuccessDarkExt = Color(0xFF1B5E20)
val SuccessContainer = Color(0xFFE8F5E9)
val OnSuccessContainer = Color(0xFF1B5E20)

val Warning = Color(0xFFFF9800)
val WarningLightExt = Color(0xFFFFB74D)
val WarningDarkExt = Color(0xFFE65100)
val WarningContainer = Color(0xFFFFF3E0)
val OnWarningContainer = Color(0xFFE65100)

val Info = Color(0xFF1E88E5)
val InfoLightExt = Color(0xFF64B5F6)
val InfoDarkExt = Color(0xFF0D47A1)
val InfoContainer = Color(0xFFE3F2FD)
val OnInfoContainer = Color(0xFF0D47A1)

// 背景色扩展
val Background = BackgroundLight
val BackgroundLightExt = Color(0xFFFFFFFF)
val BackgroundDarkExt = Color(0xFFF5F5F5)
val OnBackground = OnBackgroundLight

// 表面色扩展
val Surface = SurfaceLight
val SurfaceLightExt = Color(0xFFFFFFFF)
val SurfaceDarkExt = Color(0xFFF5F5F5)
val OnSurface = OnSurfaceLight
val SurfaceVariant = SurfaceVariantLight
val OnSurfaceVariant = OnSurfaceVariantLight

// 轮廓扩展
val Outline = OutlineLight
val OutlineVariant = OutlineVariantLight
val DividerColor = Color(0xFFE0E0E0)

// 反转色扩展
val InverseSurface = InverseSurfaceLight
val InverseOnSurface = InverseOnSurfaceLight
val InversePrimary = InversePrimaryLight

// 表面着色扩展
val SurfaceTint = SurfaceTintLight

// 表面层级颜色
val SurfaceDim = Color(0xFFE0E0E0)
val SurfaceBright = Color(0xFFFFFFFF)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFF5F5F5)
val SurfaceContainer = Color(0xFFEEEEEE)
val SurfaceContainerHigh = Color(0xFFE0E0E0)
val SurfaceContainerHighest = Color(0xFFE0E0E0)

// 文本颜色层级
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
val TextHint = Color(0xFFBDBDBD)
val TextDisabled = Color(0xFFBDBDBD)
val TextLink = Color(0xFF1E88E5)

// 卡片颜色
val CardBackground = Color(0xFFFFFFFF)
val CardBackgroundVariant = Color(0xFFF5F5F5)

// 浮动按钮颜色
val FabBackground = Color(0xFF1E88E5)
val FabContent = Color(0xFFFFFFFF)

// 阴影颜色
val ShadowColor = Color(0x20000000)
val ElevatedShadowColor = Color(0x40000000)

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
 *
 * 深色主题适配：
 * 1. 背景色使用深色（#121212）作为主背景
 * 2. 表面色使用稍浅的深色（#1E1E1E）
 * 3. 主色调使用更柔和的色调以提高可读性
 * 4. 确保所有文本在深色背景上有足够的对比度
 */

/**
 * 色彩评估指标
 * 1. 色彩对比度：确保文本与背景对比度符合WCAG 2.1 AA标准
 * 2. 色彩一致性：同一类型元素在不同页面使用相同色彩
 * 3. 色彩和谐度：通过色彩理论确保整体视觉和谐
 * 4. 色彩可访问性：考虑色盲用户的体验，使用多种视觉提示
 */
