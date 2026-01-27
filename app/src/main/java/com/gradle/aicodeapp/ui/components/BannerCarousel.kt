package com.gradle.aicodeapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gradle.aicodeapp.network.model.Banner
import com.gradle.aicodeapp.ui.theme.Animations
import com.gradle.aicodeapp.ui.theme.Shapes
import com.gradle.aicodeapp.ui.theme.Sizes
import com.gradle.aicodeapp.ui.theme.Spacing
import kotlinx.coroutines.delay

@Composable
fun BannerCarousel(
    banners: List<Banner>,
    onBannerClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) return

    val lazyListState = rememberLazyListState()
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = banners.size) {
        while (true) {
            delay(Animations.BannerAutoScrollDelay)
            currentIndex = (currentIndex + 1) % banners.size
            lazyListState.animateScrollToItem(currentIndex)
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val maxHeight = constraints.maxHeight.dp
        val bannerHeight = if (maxHeight > 300.dp) 200.dp else maxHeight * 0.6f

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)
                .padding(horizontal = Spacing.ScreenPadding, vertical = Spacing.Small)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(Shapes.Small)
            ) {
                LazyRow(
                    state = lazyListState,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
                ) {
                    items(banners) {
                        BannerItem(banner = it, onBannerClick = onBannerClick)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.Small),
                contentAlignment = Alignment.Center
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
                ) {
                    items(banners.size) {
                        Indicator(
                            isActive = it == currentIndex
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BannerItem(
    banner: Banner,
    onBannerClick: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "backgroundColor"
    )

    BoxWithConstraints {
        val cardWidth = if (constraints.maxWidth < 400) {
            constraints.maxWidth.dp - 32.dp // 减去左右边距
        } else {
            300.dp
        }

        Card(
            modifier = Modifier
                .width(cardWidth)
                .height(Sizes.BannerItemHeight.dp)
                .scale(scale)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = true),
                    onClick = {
                        if (banner.url.isNotBlank()) {
                            onBannerClick(banner.url)
                        }
                    }
                ),
            shape = Shapes.Medium,
            elevation = CardDefaults.cardElevation(
                defaultElevation = Spacing.ElevationLow,
                pressedElevation = Spacing.ElevationMedium
            ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(banner.imagePath)
                        .crossfade(true)
                        .build(),
                    contentDescription = banner.desc,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(Sizes.BannerItemHeight.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                        .padding(Spacing.CardPadding)
                ) {
                    Text(
                        text = banner.title,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Indicator(
    isActive: Boolean
) {
    Surface(
        modifier = Modifier
            .width(if (isActive) 24.dp else 8.dp)
            .height(6.dp),
        shape = Shapes.ExtraSmall,
        color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    ) {}
}
