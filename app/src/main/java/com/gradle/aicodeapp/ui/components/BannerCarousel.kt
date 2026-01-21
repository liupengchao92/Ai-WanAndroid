package com.gradle.aicodeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.BannerHeight.dp)
            .padding(horizontal = Spacing.ScreenPadding, vertical = Spacing.Small)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(Shapes.Medium)
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

@Composable
fun BannerItem(
    banner: Banner,
    onBannerClick: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Card(
        modifier = Modifier
            .width(Sizes.BannerWidth.dp)
            .height(Sizes.BannerItemHeight.dp)
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
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall
                )
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
