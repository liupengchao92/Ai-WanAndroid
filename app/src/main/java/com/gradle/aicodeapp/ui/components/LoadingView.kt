package com.gradle.aicodeapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.ui.theme.Spacing

/**
 * 加载视图组件
 * 提供统一的加载UI，支持多种加载样式和尺寸
 *
 * 使用场景：
 * - 页面初始加载
 * - 下拉刷新
 * - 加载更多
 * - 操作等待
 */

/**
 * 加载视图尺寸
 */
enum class LoadingSize(val indicatorSize: Dp, val strokeWidth: Dp) {
    SMALL(24.dp, 2.dp),
    MEDIUM(32.dp, 3.dp),
    LARGE(48.dp, 4.dp),
    EXTRA_LARGE(64.dp, 5.dp)
}

/**
 * 加载视图样式
 */
enum class LoadingStyle {
    CIRCULAR,
    LINEAR,
    DOTS
}

/**
 * 全屏加载视图
 * 适用于页面初始加载场景
 *
 * @param text 加载提示文本，默认为null不显示
 * @param size 加载指示器尺寸，默认为LARGE
 * @param style 加载样式，默认为CIRCULAR
 * @param modifier 修饰符
 * @param showShadow 是否显示阴影背景，默认为true
 * @param backgroundColor 背景颜色，默认为surface颜色
 */
@Composable
fun FullScreenLoadingView(
    text: String? = null,
    size: LoadingSize = LoadingSize.LARGE,
    style: LoadingStyle = LoadingStyle.CIRCULAR,
    modifier: Modifier = Modifier,
    showShadow: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.Large)
        ) {
            LoadingIndicator(
                size = size,
                style = style,
                showShadow = showShadow,
                backgroundColor = backgroundColor
            )

            if (text != null) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * 行内加载视图
 * 适用于列表底部加载更多、局部加载场景
 *
 * @param text 加载提示文本，默认为null不显示
 * @param size 加载指示器尺寸，默认为SMALL
 * @param style 加载样式，默认为CIRCULAR
 * @param modifier 修饰符
 */
@Composable
fun InlineLoadingView(
    text: String? = null,
    size: LoadingSize = LoadingSize.SMALL,
    style: LoadingStyle = LoadingStyle.CIRCULAR,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Large),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            LoadingIndicator(
                size = size,
                style = style,
                showShadow = false
            )

            if (text != null) {
                Spacer(modifier = Modifier.width(Spacing.Medium))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * 居中加载视图
 * 适用于局部区域加载场景
 *
 * @param text 加载提示文本，默认为null不显示
 * @param size 加载指示器尺寸，默认为MEDIUM
 * @param style 加载样式，默认为CIRCULAR
 * @param modifier 修饰符
 * @param verticalArrangement 垂直排列方式
 */
@Composable
fun CenteredLoadingView(
    text: String? = null,
    size: LoadingSize = LoadingSize.MEDIUM,
    style: LoadingStyle = LoadingStyle.CIRCULAR,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = verticalArrangement
    ) {
        LoadingIndicator(
            size = size,
            style = style,
            showShadow = false
        )

        if (text != null) {
            Spacer(modifier = Modifier.height(Spacing.Small))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * 加载指示器
 * 基础加载动画组件
 *
 * @param size 加载指示器尺寸
 * @param style 加载样式
 * @param showShadow 是否显示阴影背景
 * @param backgroundColor 背景颜色
 * @param indicatorColor 指示器颜色，默认为primary
 * @param trackColor 轨道颜色，默认为surfaceVariant
 */
@Composable
fun LoadingIndicator(
    size: LoadingSize = LoadingSize.MEDIUM,
    style: LoadingStyle = LoadingStyle.CIRCULAR,
    showShadow: Boolean = false,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    if (showShadow) {
        Box(
            modifier = Modifier
                .size(size.indicatorSize + 52.dp)
                .shadow(
                    elevation = Spacing.ElevationHigh,
                    shape = CircleShape,
                    spotColor = indicatorColor.copy(alpha = 0.2f)
                )
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicatorContent(
                size = size,
                style = style,
                indicatorColor = indicatorColor,
                trackColor = trackColor
            )
        }
    } else {
        LoadingIndicatorContent(
            size = size,
            style = style,
            indicatorColor = indicatorColor,
            trackColor = trackColor
        )
    }
}

@Composable
private fun LoadingIndicatorContent(
    size: LoadingSize,
    style: LoadingStyle,
    indicatorColor: Color,
    trackColor: Color
) {
    when (style) {
        LoadingStyle.CIRCULAR -> {
            CircularProgressIndicator(
                modifier = Modifier.size(size.indicatorSize),
                color = indicatorColor,
                strokeWidth = size.strokeWidth,
                trackColor = trackColor,
                strokeCap = StrokeCap.Round
            )
        }
        LoadingStyle.LINEAR -> {
            LinearProgressIndicator(
                modifier = Modifier
                    .width(size.indicatorSize * 2)
                    .height(size.strokeWidth)
                    .clip(CircleShape),
                color = indicatorColor,
                trackColor = trackColor,
                strokeCap = StrokeCap.Round
            )
        }
        LoadingStyle.DOTS -> {
            DotsLoadingIndicator(
                size = size,
                color = indicatorColor
            )
        }
    }
}

/**
 * 点状加载动画
 */
@Composable
private fun DotsLoadingIndicator(
    size: LoadingSize,
    color: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots_loading")

    val dotSize = when (size) {
        LoadingSize.SMALL -> 6.dp
        LoadingSize.MEDIUM -> 8.dp
        LoadingSize.LARGE -> 10.dp
        LoadingSize.EXTRA_LARGE -> 12.dp
    }

    val dotSpacing = when (size) {
        LoadingSize.SMALL -> 4.dp
        LoadingSize.MEDIUM -> 6.dp
        LoadingSize.LARGE -> 8.dp
        LoadingSize.EXTRA_LARGE -> 10.dp
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(dotSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val delay = index * 150
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 600,
                        delayMillis = delay,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_scale_$index"
            )
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 600,
                        delayMillis = delay,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_alpha_$index"
            )

            Box(
                modifier = Modifier
                    .size(dotSize)
                    .scale(scale)
                    .alpha(alpha)
                    .background(color, CircleShape)
            )
        }
    }
}

/**
 * 加载视图使用规范
 *
 * 1. 全屏加载 (FullScreenLoadingView)
 *    - 页面初始加载
 *    - 数据刷新
 *    - 使用场景：CollectPage, HomePage等页面初始加载
 *
 * 2. 行内加载 (InlineLoadingView)
 *    - 列表底部加载更多
 *    - 局部区域加载
 *    - 使用场景：RouteListPage, ColumnListPage等列表页面
 *
 * 3. 居中加载 (CenteredLoadingView)
 *    - 局部区域加载
 *    - 对话框内加载
 *    - 使用场景：SearchPage, SettingsPage等局部加载
 *
 * 4. 尺寸选择
 *    - SMALL: 行内加载、小区域加载
 *    - MEDIUM: 标准加载指示器
 *    - LARGE: 页面主要加载区域
 *    - EXTRA_LARGE: 全屏加载
 *
 * 5. 样式选择
 *    - CIRCULAR: 通用加载场景
 *    - LINEAR: 进度指示、顶部加载条
 *    - DOTS: 轻量级加载提示
 *
 * 6. 文本提示
 *    - 页面加载：显示具体加载内容，如"加载收藏列表..."
 *    - 加载更多：显示"加载更多内容..."
 *    - 操作等待：显示操作描述，如"保存中..."
 */
