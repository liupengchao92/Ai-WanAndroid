package com.gradle.aicodeapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.ui.theme.CustomShapes
import com.gradle.aicodeapp.ui.theme.Spacing

/**
 * 按钮系统规范
 * 基于Material 3设计系统，定义了应用的完整按钮体系
 */

/**
 * 主要按钮
 * 应用场景：主要操作、确认操作、提交操作等
 */
@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    iconPosition: IconPosition = IconPosition.LEFT,
    size: ButtonSize = ButtonSize.MEDIUM
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            isPressed -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            isHovered -> MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 200)
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.onPrimary
        },
        animationSpec = tween(durationMillis = 200)
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = backgroundColor,
        contentColor = contentColor,
        disabledContainerColor = backgroundColor,
        disabledContentColor = contentColor
    )

    val buttonModifier = modifier
        .height(size.height.dp)
        .scale(scale)

    Button(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        colors = buttonColors,
        shape = CustomShapes.Medium,
        interactionSource = interactionSource
    ) {
        ButtonContent(
            text = text,
            icon = icon,
            iconPosition = iconPosition
        )
    }
}

/**
 * 次要按钮
 * 应用场景：次要操作、取消操作、返回操作等
 */
@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    iconPosition: IconPosition = IconPosition.LEFT,
    size: ButtonSize = ButtonSize.MEDIUM
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            isPressed -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            isHovered -> MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 200)
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 200)
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    val buttonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = contentColor,
        disabledContentColor = contentColor
    )

    val buttonModifier = modifier
        .height(size.height.dp)
        .scale(scale)

    OutlinedButton(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        colors = buttonColors,
        border = BorderStroke(1.dp, borderColor),
        shape = CustomShapes.Medium,
        interactionSource = interactionSource
    ) {
        ButtonContent(
            text = text,
            icon = icon,
            iconPosition = iconPosition
        )
    }
}

/**
 * 文本按钮
 * 应用场景：链接操作、辅助操作、详细信息等
 */
@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    iconPosition: IconPosition = IconPosition.LEFT
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            isPressed -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            isHovered -> MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 200)
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    val buttonColors = ButtonDefaults.textButtonColors(
        contentColor = contentColor,
        disabledContentColor = contentColor
    )

    val buttonModifier = modifier
        .scale(scale)

    TextButton(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        colors = buttonColors,
        interactionSource = interactionSource
    ) {
        ButtonContent(
            text = text,
            icon = icon,
            iconPosition = iconPosition
        )
    }
}

/**
 * 按钮内容
 */
@Composable
private fun ButtonContent(
    text: String,
    icon: Painter?,
    iconPosition: IconPosition
) {
    Row(
        modifier = Modifier.padding(horizontal = Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null && iconPosition == IconPosition.LEFT) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(Spacing.Small))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
        if (icon != null && iconPosition == IconPosition.RIGHT) {
            Spacer(modifier = Modifier.width(Spacing.Small))
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/**
 * 按钮尺寸
 */
enum class ButtonSize(val height: Int) {
    SMALL(36),
    MEDIUM(48),
    LARGE(56)
}

/**
 * 图标位置
 */
enum class IconPosition {
    LEFT,
    RIGHT
}

/**
 * 按钮交互规则
 * 1. 点击反馈：按钮被点击时应显示视觉反馈（颜色变化、缩放效果）
 * 2. 悬停反馈：鼠标悬停时应显示视觉反馈（颜色变化）
 * 3. 禁用状态：禁用时应显示禁用视觉效果（透明度降低）
 * 4. 动画效果：状态变化时应使用平滑的动画过渡
 * 5. 触摸目标：按钮高度至少为48dp，确保足够的触摸区域
 */

/**
 * 按钮评估指标
 * 1. 交互一致性：相同类型按钮有相同的交互行为
 * 2. 视觉一致性：相同类型按钮有相同的视觉样式
 * 3. 反馈及时性：用户操作后有即时、明确的反馈
 * 4. 可访问性：按钮文本清晰，禁用状态明显
 * 5. 性能表现：动画效果流畅，无卡顿
 */
