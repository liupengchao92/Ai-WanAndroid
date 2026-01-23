package com.gradle.aicodeapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.network.exception.ErrorHandler

@Composable
fun ErrorDialog(
    title: String = "提示",
    message: String,
    errorType: ErrorHandler.ErrorType = ErrorHandler.ErrorType.UNKNOWN,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon: androidx.compose.ui.graphics.vector.ImageVector
    val iconColor: Color
    
    when (errorType) {
        ErrorHandler.ErrorType.NOT_LOGIN -> {
            icon = Icons.Default.Info
            iconColor = Color(0xFF2196F3)
        }
        ErrorHandler.ErrorType.COLLECT_NOT_LOGIN -> {
            icon = Icons.Default.Info
            iconColor = Color(0xFF2196F3)
        }
        ErrorHandler.ErrorType.NETWORK -> {
            icon = Icons.Default.Warning
            iconColor = Color(0xFFFF9800)
        }
        ErrorHandler.ErrorType.TIMEOUT -> {
            icon = Icons.Default.Warning
            iconColor = Color(0xFFFF9800)
        }
        ErrorHandler.ErrorType.SERVER -> {
            icon = Icons.Default.Close
            iconColor = Color(0xFFF44336)
        }
        ErrorHandler.ErrorType.PARSE -> {
            icon = Icons.Default.Close
            iconColor = Color(0xFFF44336)
        }
        ErrorHandler.ErrorType.UNKNOWN -> {
            icon = Icons.Default.Close
            iconColor = Color(0xFFF44336)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = message)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = getErrorHint(errorType),
                    color = Color.Gray,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "确定")
            }
        },
        modifier = modifier.padding(16.dp)
    )
}

@Composable
private fun getErrorHint(errorType: ErrorHandler.ErrorType): String {
    return when (errorType) {
        ErrorHandler.ErrorType.NOT_LOGIN -> "请登录后重试"
        ErrorHandler.ErrorType.COLLECT_NOT_LOGIN -> "请登录后重试"
        ErrorHandler.ErrorType.NETWORK -> "请检查网络连接后重试"
        ErrorHandler.ErrorType.TIMEOUT -> "请稍后重试"
        ErrorHandler.ErrorType.SERVER -> "服务器繁忙，请稍后重试"
        ErrorHandler.ErrorType.PARSE -> "数据解析失败，请联系客服"
        ErrorHandler.ErrorType.UNKNOWN -> "请稍后重试"
    }
}
