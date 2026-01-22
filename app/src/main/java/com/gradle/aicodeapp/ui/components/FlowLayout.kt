package com.gradle.aicodeapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class FlowLayoutParams(
    val horizontalSpacing: Dp = 8.dp,
    val verticalSpacing: Dp = 8.dp,
    val padding: PaddingValues = PaddingValues(0.dp)
)

@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val verticalSpacingPx = verticalSpacing.roundToPx()

        val placeables = measurables.map { measurable ->
            measurable.measure(
                Constraints(
                    minWidth = 0,
                    maxWidth = constraints.maxWidth,
                    minHeight = 0,
                    maxHeight = constraints.maxHeight
                )
            )
        }

        val rows = mutableListOf<List<Placeable>>()
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0

        for (placeable in placeables) {
            val spacingNeeded = if (currentRow.isEmpty()) 0 else horizontalSpacingPx
            val wouldExceedWidth = currentRowWidth + spacingNeeded + placeable.width > constraints.maxWidth

            if (wouldExceedWidth && currentRow.isNotEmpty()) {
                rows.add(currentRow.toList())
                currentRow = mutableListOf(placeable)
                currentRowWidth = placeable.width
            } else {
                currentRow.add(placeable)
                currentRowWidth += spacingNeeded + placeable.width
            }
        }

        if (currentRow.isNotEmpty()) {
            rows.add(currentRow.toList())
        }

        val totalHeight = rows.sumOf { row ->
            row.maxOfOrNull { it.height } ?: 0
        } + (rows.size - 1) * verticalSpacingPx

        layout(
            width = constraints.maxWidth,
            height = totalHeight.coerceAtLeast(constraints.minHeight)
        ) {
            var yPosition = 0

            for (row in rows) {
                var xPosition = 0
                val rowHeight = row.maxOfOrNull { it.height } ?: 0

                for (placeable in row) {
                    placeable.place(x = xPosition, y = yPosition)
                    xPosition += placeable.width + horizontalSpacingPx
                }

                yPosition += rowHeight + verticalSpacingPx
            }
        }
    }
}

@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    params: FlowLayoutParams = FlowLayoutParams(),
    content: @Composable () -> Unit
) {
    FlowLayout(
        modifier = modifier,
        horizontalSpacing = params.horizontalSpacing,
        verticalSpacing = params.verticalSpacing,
        content = content
    )
}
