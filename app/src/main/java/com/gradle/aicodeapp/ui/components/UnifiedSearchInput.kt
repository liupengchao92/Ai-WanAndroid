package com.gradle.aicodeapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.ui.theme.Spacing

/**
 * 统一的搜索输入框组件
 * 用于 SquarePage 和 SearchPage 保持视觉一致性
 *
 * @param query 当前搜索文本
 * @param onQueryChange 搜索文本变化回调
 * @param onSearch 执行搜索回调
 * @param onClear 清除搜索回调
 * @param focusRequester 焦点请求器
 * @param onFocusChange 焦点状态变化回调
 * @param placeholder 占位符文本
 * @param modifier 修饰符
 */
@Composable
fun UnifiedSearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    onFocusChange: (Boolean) -> Unit,
    placeholder: String = "搜索文章、项目、教程...",
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onFocusChange(isFocused)
    }

    val containerColor = if (isFocused) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }

    val elevation by animateDpAsState(
        targetValue = if (isFocused) 2.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "elevation"
    )

    Surface(
        modifier = modifier
            .height(44.dp)
            .shadow(elevation, RoundedCornerShape(Spacing.CornerRadiusFull)),
        shape = RoundedCornerShape(Spacing.CornerRadiusFull),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(Spacing.Small))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        onSearch(query)
                    }
                ),
                interactionSource = interactionSource
            )

            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = scaleIn(animationSpec = spring(stiffness = Spring.StiffnessHigh)) + fadeIn(),
                exit = scaleOut(animationSpec = spring(stiffness = Spring.StiffnessHigh)) + fadeOut()
            ) {
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "清除",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 只读搜索框组件（用于点击跳转搜索页面）
 *
 * @param placeholder 占位符文本
 * @param onClick 点击回调
 * @param modifier 修饰符
 */
@Composable
fun UnifiedSearchBox(
    placeholder: String = "搜索文章、项目、教程...",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(44.dp),
        shape = RoundedCornerShape(Spacing.CornerRadiusFull),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(Spacing.Small))

            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
