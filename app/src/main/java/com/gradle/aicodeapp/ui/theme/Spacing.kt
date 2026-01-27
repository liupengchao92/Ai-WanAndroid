package com.gradle.aicodeapp.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Spacing {
    // 基础间距系统 - 基于4dp网格
    val ExtraSmall: Dp = 4.dp
    val Small: Dp = 8.dp
    val Medium: Dp = 12.dp
    val Large: Dp = 16.dp
    val ExtraLarge: Dp = 24.dp
    val Huge: Dp = 32.dp
    val ExtraHuge: Dp = 48.dp

    // 屏幕边距
    val ScreenPadding: Dp = 16.dp
    val ScreenPaddingCompact: Dp = 12.dp
    val ScreenPaddingComfortable: Dp = 20.dp
    val ScreenPaddingExpanded: Dp = 24.dp
    val ScreenPaddingExtraExpanded: Dp = 32.dp
    
    // 内容最大宽度
    val ContentMaxWidth: Dp = 1000.dp

    // 卡片内边距
    val CardPadding: Dp = 16.dp
    val CardPaddingSmall: Dp = 12.dp
    val CardPaddingLarge: Dp = 20.dp

    // 内容内边距
    val ContentPadding: Dp = 12.dp
    val ContentPaddingSmall: Dp = 8.dp
    val ContentPaddingLarge: Dp = 16.dp

    // 按钮高度
    val ButtonHeight: Dp = 48.dp
    val ButtonHeightSmall: Dp = 40.dp
    val ButtonHeightLarge: Dp = 56.dp

    // 输入框高度
    val InputHeight: Dp = 56.dp
    val InputHeightSmall: Dp = 48.dp
    val InputHeightLarge: Dp = 64.dp

    // 图标尺寸
    val IconSmall: Dp = 16.dp
    val IconMedium: Dp = 24.dp
    val IconLarge: Dp = 32.dp
    val IconExtraLarge: Dp = 48.dp
    val IconHuge: Dp = 80.dp

    // 边框宽度
    val BorderWidthThin: Dp = 1.dp
    val BorderWidthMedium: Dp = 2.dp
    val BorderWidthThick: Dp = 3.dp

    // 阴影高度
    val ElevationNone: Dp = 0.dp
    val ElevationLow: Dp = 2.dp
    val ElevationMedium: Dp = 4.dp
    val ElevationHigh: Dp = 8.dp
    val ElevationExtraHigh: Dp = 12.dp

    // 圆角半径
    val CornerRadiusSmall: Dp = 4.dp
    val CornerRadiusMedium: Dp = 8.dp
    val CornerRadiusLarge: Dp = 12.dp
    val CornerRadiusExtraLarge: Dp = 16.dp
    val CornerRadiusFull: Dp = 28.dp
    val CornerRadiusSearch: Dp = 24.dp
    val CornerRadiusCard: Dp = 12.dp

    // 列表项间距
    val ListItemSpacing: Dp = 8.dp
    val ListItemSpacingCompact: Dp = 4.dp
    val ListItemSpacingComfortable: Dp = 12.dp

    // 标题栏高度
    val TopBarHeight: Dp = 56.dp
    val TopBarHeightLarge: Dp = 64.dp

    // 底部导航栏高度
    val BottomNavHeight: Dp = 56.dp

    // 浮动按钮尺寸
    val FabSize: Dp = 56.dp
    val FabSizeSmall: Dp = 40.dp
    val FabSizeLarge: Dp = 64.dp

    // 预定义内边距集合
    val ContentPaddingValues: PaddingValues = PaddingValues(
        horizontal = ScreenPadding,
        vertical = Medium
    )

    val CardPaddingValues: PaddingValues = PaddingValues(
        horizontal = CardPadding,
        vertical = CardPadding
    )

    val ButtonPaddingValues: PaddingValues = PaddingValues(
        horizontal = Large,
        vertical = Medium
    )

    val ScreenPaddingValues: PaddingValues = PaddingValues(
        horizontal = ScreenPadding,
        vertical = Small
    )

    // 行高
    val LineHeightSmall: Dp = 16.dp
    val LineHeightMedium: Dp = 20.dp
    val LineHeightLarge: Dp = 24.dp
    val LineHeightExtraLarge: Dp = 28.dp

    // 间距比例
    val SpacingScale: Dp = 4.dp
}