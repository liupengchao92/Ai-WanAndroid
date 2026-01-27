package com.gradle.aicodeapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gradle.aicodeapp.network.model.PopularColumn
import com.gradle.aicodeapp.network.model.PopularRoute
import com.gradle.aicodeapp.network.model.PopularWenda
import com.gradle.aicodeapp.ui.theme.CustomShapes
import com.gradle.aicodeapp.ui.theme.Spacing
import com.gradle.aicodeapp.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun PopularCardsSection(
    viewModel: HomeViewModel = hiltViewModel(),
    onWendaClick: (String, String) -> Unit = { _, _ -> },
    onColumnClick: (String, String) -> Unit = { _, _ -> },
    onRouteClick: (String, String) -> Unit = { _, _ -> },
    onViewMoreWenda: () -> Unit = {},
    onViewMoreColumn: () -> Unit = {},
    onViewMoreRoute: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    var wendaData by remember { mutableStateOf<List<PopularWenda>>(emptyList()) }
    var columnData by remember { mutableStateOf<List<PopularColumn>>(emptyList()) }
    var routeData by remember { mutableStateOf<List<PopularRoute>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                isLoading = true
                val wendaResult = viewModel.getPopularWenda()
                val columnResult = viewModel.getPopularColumn()
                val routeResult = viewModel.getPopularRoute()

                wendaData = wendaResult.take(3)
                columnData = columnResult.take(3)
                routeData = routeResult.take(3)
                isLoading = false
            } catch (e: Exception) {
                errorMessage = e.message ?: "加载失败"
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.ScreenPadding, vertical = Spacing.Medium)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "加载失败",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            val scrollState = rememberScrollState()
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val screenWidth = constraints.maxWidth.dp
                val cardWidth = when {
                    screenWidth > 600.dp -> 300.dp
                    screenWidth > 400.dp -> screenWidth * 0.8f
                    else -> screenWidth * 0.9f
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(state = scrollState),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
                ) {
                    PopularWendaCard(
                        wendaList = wendaData,
                        onWendaClick = onWendaClick,
                        onViewMore = onViewMoreWenda,
                        modifier = Modifier.width(cardWidth)
                    )

                    PopularColumnCard(
                        columnList = columnData,
                        onColumnClick = onColumnClick,
                        onViewMore = onViewMoreColumn,
                        modifier = Modifier.width(cardWidth)
                    )

                    PopularRouteCard(
                        routeList = routeData,
                        onRouteClick = onRouteClick,
                        onViewMore = onViewMoreRoute,
                        modifier = Modifier.width(cardWidth)
                    )
                }
            }
        }
    }
}

@Composable
fun PopularWendaCard(
    wendaList: List<PopularWenda>,
    onWendaClick: (String, String) -> Unit,
    onViewMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CustomShapes.Medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = Spacing.ElevationLow
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(Spacing.CardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "最受欢迎回答",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                ViewMoreButton(onClick = onViewMore)
            }

            Spacer(modifier = Modifier.height(Spacing.Small))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                wendaList.forEach { wenda ->
                    WendaItem(
                        title = wenda.title,
                        onClick = { onWendaClick(wenda.link, wenda.title) }
                    )
                    Spacer(modifier = Modifier.height(Spacing.Small))
                }
            }
        }
    }
}

@Composable
fun PopularColumnCard(
    columnList: List<PopularColumn>,
    onColumnClick: (String, String) -> Unit,
    onViewMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CustomShapes.Medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = Spacing.ElevationLow
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(Spacing.CardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "最受欢迎专栏",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                ViewMoreButton(onClick = onViewMore)
            }

            Spacer(modifier = Modifier.height(Spacing.Small))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                columnList.forEach { column ->
                    ColumnItem(
                        name = column.name,
                        onClick = { onColumnClick(column.url, column.name) }
                    )
                    Spacer(modifier = Modifier.height(Spacing.Small))
                }
            }
        }
    }
}

@Composable
fun PopularRouteCard(
    routeList: List<PopularRoute>,
    onRouteClick: (String, String) -> Unit,
    onViewMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CustomShapes.Medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = Spacing.ElevationLow
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(Spacing.CardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "最新学习路线",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                ViewMoreButton(onClick = onViewMore)
            }

            Spacer(modifier = Modifier.height(Spacing.Small))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                routeList.forEach { route ->
                    RouteItem(
                        name = route.name,
                        onClick = {
                            onRouteClick(
                                "https://www.wanandroid.com/show/${route.id}",
                                route.name
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(Spacing.Small))
                }
            }
        }
    }
}

@Composable
fun WendaItem(
    title: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isPressed -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "backgroundColor"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(CustomShapes.Small)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(backgroundColor)
            .padding(horizontal = Spacing.Small)
            .scale(scale),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ColumnItem(
    name: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isPressed -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "backgroundColor"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(CustomShapes.Small)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(backgroundColor)
            .padding(horizontal = Spacing.Small)
            .scale(scale),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RouteItem(
    name: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isPressed -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "backgroundColor"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(CustomShapes.Small)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(backgroundColor)
            .padding(horizontal = Spacing.Small)
            .scale(scale),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ViewMoreButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .height(32.dp)
            .widthIn(min = 60.dp),
        shape = CustomShapes.Small
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "更多",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(16.dp)
            )
        }
    }
}
