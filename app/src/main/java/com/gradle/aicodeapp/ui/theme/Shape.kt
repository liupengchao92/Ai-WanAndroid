package com.gradle.aicodeapp.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * 形状系统规范
 * 基于Material 3设计系统，定义了应用的完整形状体系
 */

// Material 3 标准形状配置
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

// 自定义形状变体
object CustomShapes {
    val ExtraSmall: CornerBasedShape = RoundedCornerShape(4.dp)
    val Small: CornerBasedShape = RoundedCornerShape(8.dp)
    val Medium: CornerBasedShape = RoundedCornerShape(12.dp)
    val Large: CornerBasedShape = RoundedCornerShape(16.dp)
    val ExtraLarge: CornerBasedShape = RoundedCornerShape(24.dp)
    val Full: CornerBasedShape = RoundedCornerShape(50)

    val TopSmall: CornerBasedShape = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    val BottomSmall: CornerBasedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 8.dp,
        bottomEnd = 8.dp
    )

    val TopMedium: CornerBasedShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    val BottomMedium: CornerBasedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )

    val TopLarge: CornerBasedShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    val BottomLarge: CornerBasedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
}

/**
 * 形状应用规则
 * 1. 小形状（Small）：用于小按钮、标签、徽章等小型元素
 * 2. 中形状（Medium）：用于卡片、输入框、对话框等中型元素
 * 3. 大形状（Large）：用于大卡片、模态框等大型元素
 * 4. 自定义形状：用于特殊场景，如只需要顶部圆角的元素
 * 5. 圆形（Full）：用于圆形按钮、头像等圆形元素
 */

/**
 * 形状评估指标
 * 1. 形状一致性：同一类型元素使用相同的圆角半径
 * 2. 形状层次：根据元素大小和重要性使用不同大小的圆角
 * 3. 形状和谐：确保圆角大小与元素尺寸成比例
 * 4. 响应式适配：在不同屏幕尺寸下保持形状的一致性
 */