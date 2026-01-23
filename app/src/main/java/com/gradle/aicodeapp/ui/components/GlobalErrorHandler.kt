package com.gradle.aicodeapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.gradle.aicodeapp.network.exception.ErrorHandler
import com.gradle.aicodeapp.utils.ToastUtils
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GlobalErrorHandler(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    var showLoginDialog by remember { mutableStateOf(false) }
    var showCollectLoginDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var errorType by remember { mutableStateOf(ErrorHandler.ErrorType.UNKNOWN) }

    LaunchedEffect(Unit) {
        ErrorHandler.errorEvent.collectLatest { event ->
            when (event.type) {
                ErrorHandler.ErrorType.NOT_LOGIN -> {
                    showLoginDialog = true
                }
                ErrorHandler.ErrorType.COLLECT_NOT_LOGIN -> {
                    showCollectLoginDialog = true
                }
                else -> {
                    errorMessage = event.message
                    errorType = event.type
                    showErrorDialog = true
                }
            }
        }
    }

    if (showLoginDialog) {
        LoginRequiredDialog(
            onDismiss = {
                showLoginDialog = false
            },
            onConfirm = {
                showLoginDialog = false
                onNavigateToLogin()
            }
        )
    }

    if (showCollectLoginDialog) {
        CollectLoginRequiredDialog(
            onDismiss = {
                showCollectLoginDialog = false
            },
            onNavigateToLogin = {
                showCollectLoginDialog = false
                onNavigateToLogin()
            }
        )
    }

    if (showErrorDialog) {
        ErrorDialog(
            title = "提示",
            message = errorMessage,
            errorType = errorType,
            onDismiss = {
                showErrorDialog = false
            },
            modifier = modifier
        )
    }
}
