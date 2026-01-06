package com.gradle.aicodeapp.ui.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gradle.aicodeapp.network.model.Banner
import kotlinx.coroutines.delay

@Composable
fun BannerCarousel(
    banners: List<Banner>,
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) return

    val lazyListState = rememberLazyListState()
    var currentIndex by remember { mutableIntStateOf(0) }

    // 自动轮播
    LaunchedEffect(key1 = banners.size) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % banners.size
            lazyListState.animateScrollToItem(currentIndex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyRow(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(banners) {
                    BannerItem(banner = it)
                }
            }
        }

        // 指示器
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
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
    banner: Banner
) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Banner 图片
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(banner.imagePath)
                    .crossfade(true)
                    .build(),
                contentDescription = banner.desc,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(180.dp)
            )

            // Banner 标题
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = banner.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun Indicator(
    isActive: Boolean
) {
    Box(
        modifier = Modifier
            .width(if (isActive) 16.dp else 6.dp)
            .height(6.dp)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().height(4.dp),
            shape = RoundedCornerShape(2.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isActive) Color.Blue else Color.Gray
            )
        ) {}
    }
}
