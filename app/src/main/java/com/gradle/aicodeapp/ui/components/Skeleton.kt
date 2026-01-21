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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.ui.theme.Shapes
import com.gradle.aicodeapp.ui.theme.Spacing

@Composable
fun ArticleItemSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.ScreenPadding, vertical = Spacing.Small),
        shape = Shapes.Medium,
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.ElevationLow),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.CardPadding)
        ) {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )

            Spacer(modifier = Modifier.height(Spacing.Small))

            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SkeletonBox(
                        modifier = Modifier
                            .width(60.dp)
                            .height(14.dp)
                    )

                    Spacer(modifier = Modifier.width(Spacing.Medium))

                    SkeletonBox(
                        modifier = Modifier
                            .width(50.dp)
                            .height(14.dp)
                    )

                    Spacer(modifier = Modifier.width(Spacing.Medium))

                    SkeletonBox(
                        modifier = Modifier
                            .width(40.dp)
                            .height(14.dp)
                    )
                }

                SkeletonBox(
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

@Composable
fun BannerSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = Spacing.ScreenPadding, vertical = Spacing.Small),
        shape = Shapes.Medium,
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.ElevationLow),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        SkeletonBox(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.surfaceVariant
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = shimmerTranslate, y = shimmerTranslate)
    )

    Box(
        modifier = modifier
            .background(
                brush = brush,
                shape = RoundedCornerShape(4.dp)
            )
    )
}