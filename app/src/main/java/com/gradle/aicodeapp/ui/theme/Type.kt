package com.gradle.aicodeapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 排版系统规范
 * 基于Material 3设计系统，定义了应用的完整排版体系
 */

// 字体选择：使用系统默认字体，确保跨设备兼容性
// 建议在未来版本中考虑引入无衬线字体提升现代感
val AppFontFamily = FontFamily.Default

/**
 * 字重体系
 * 1. Regular (W400)：用于普通正文文本
 * 2. Medium (W500)：用于标题、按钮文本等需要强调的元素
 * 3. Bold (W700)：用于重要标题、强调文本等
 */
val AppFontWeightRegular = FontWeight.W400
val AppFontWeightMedium = FontWeight.W500
val AppFontWeightBold = FontWeight.W700

val Typography = Typography(
    // 显示型文本 - 用于大标题和营销内容
    // 应用场景：应用启动页、营销活动页面、品牌展示区域
    displayLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightRegular,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightRegular,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightRegular,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // 标题文本 - 用于页面和区块标题
    // 应用场景：页面顶部标题、主要内容区块标题
    headlineLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightRegular,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightRegular,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightRegular,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // 标题文本 - 用于卡片标题和列表项标题
    // 应用场景：卡片标题、列表项标题、导航栏标题
    titleLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightMedium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightMedium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightMedium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // 正文文本 - 用于主要内容
    // 应用场景：文章正文、详细说明、用户输入文本
    bodyLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightRegular,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightRegular,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightRegular,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // 标签文本 - 用于按钮、标签等
    // 应用场景：按钮文本、标签、提示信息、徽章
    labelLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightMedium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightMedium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = AppFontWeightMedium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * 排版应用规则
 * 1. 大标题：使用displayLarge/Medium/Small，用于营销页面和品牌展示
 * 2. 页面标题：使用headlineLarge/Medium/Small，用于页面顶部标题
 * 3. 区块标题：使用titleLarge/Medium/Small，用于卡片、列表等区块标题
 * 4. 正文文本：使用bodyLarge/Medium/Small，用于主要内容和说明文字
 * 5. 辅助文本：使用labelLarge/Medium/Small，用于按钮、标签和提示信息
 * 6. 强调文本：对于需要强调的文本，可使用对应字号的Medium或Bold字重
 * 7. 文本对齐：主要内容使用左对齐，标题可根据设计需要使用居中对齐
 */

/**
 * 排版评估指标
 * 1. 字体层级：确保页面有清晰的视觉层次结构，字号大小有明显区分
 * 2. 文本可读性：行高设置为字号的1.5倍左右，确保舒适的阅读体验
 * 3. 排版一致性：相同类型内容使用相同文本样式，保持视觉统一
 * 4. 响应式适配：在不同屏幕尺寸下调整字号大小，确保在小屏幕上的可读性
 * 5. 文本密度：根据内容类型调整文本密度，重要内容使用更大的行高和字间距
 */

/**
 * 响应式排版建议
 * 1. 小屏幕（< 600dp）：使用较小的字号，确保内容完整显示
 * 2. 中屏幕（600dp - 840dp）：使用标准字号，平衡可读性和空间利用
 * 3. 大屏幕（> 840dp）：使用较大的字号，提升阅读体验
 * 4. 关键文本：确保关键文本在所有屏幕尺寸下都清晰可见
 */