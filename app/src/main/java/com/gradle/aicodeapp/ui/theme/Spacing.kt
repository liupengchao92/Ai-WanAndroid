package com.gradle.aicodeapp.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Spacing {
    val ExtraSmall: Dp = 4.dp
    val Small: Dp = 8.dp
    val Medium: Dp = 12.dp
    val Large: Dp = 16.dp
    val ExtraLarge: Dp = 24.dp
    val Huge: Dp = 32.dp
    val ExtraHuge: Dp = 48.dp

    val ScreenPadding: Dp = 16.dp
    val CardPadding: Dp = 16.dp
    val ContentPadding: Dp = 12.dp

    val ButtonHeight: Dp = 48.dp
    val InputHeight: Dp = 56.dp

    val IconSmall: Dp = 16.dp
    val IconMedium: Dp = 24.dp
    val IconLarge: Dp = 32.dp
    val IconExtraLarge: Dp = 48.dp
    val IconHuge: Dp = 80.dp

    val BorderWidthThin: Dp = 1.dp
    val BorderWidthMedium: Dp = 2.dp

    val ElevationNone: Dp = 0.dp
    val ElevationLow: Dp = 2.dp
    val ElevationMedium: Dp = 4.dp
    val ElevationHigh: Dp = 8.dp

    val CornerRadiusSmall: Dp = 4.dp
    val CornerRadiusMedium: Dp = 8.dp
    val CornerRadiusLarge: Dp = 12.dp
    val CornerRadiusExtraLarge: Dp = 16.dp
    val CornerRadiusFull: Dp = 28.dp

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
}