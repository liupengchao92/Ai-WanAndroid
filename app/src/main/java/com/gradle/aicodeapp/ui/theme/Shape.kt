package com.gradle.aicodeapp.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object Shapes {
    val ExtraSmall: CornerBasedShape = RoundedCornerShape(4.dp)
    val Small: CornerBasedShape = RoundedCornerShape(8.dp)
    val Medium: CornerBasedShape = RoundedCornerShape(12.dp)
    val Large: CornerBasedShape = RoundedCornerShape(16.dp)
    val ExtraLarge: CornerBasedShape = RoundedCornerShape(24.dp)
    val Full: CornerBasedShape = RoundedCornerShape(28.dp)

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
}