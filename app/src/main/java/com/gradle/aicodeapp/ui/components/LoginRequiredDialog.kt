package com.gradle.aicodeapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginRequiredDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "登录提示")
        },
        text = {
            Text(text = "您尚未登录或登录已过期，是否前往登录页面？")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "登录")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "取消")
            }
        },
        modifier = modifier
    )
}
