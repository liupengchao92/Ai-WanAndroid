package com.gradle.aicodeapp.ui.pages

import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.gradle.aicodeapp.R
import com.gradle.aicodeapp.ui.theme.Success
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailPage(
    articleUrl: String,
    articleTitle: String = "",
    articleId: Int? = null,
    onBackClick: () -> Unit,
    onCollectClick: (Int) -> Unit = {},
    onUncollectClick: (Int) -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var loadingProgress by remember { mutableStateOf(0) }
    var showCollectDialog by remember { mutableStateOf(false) }
    var showUncollectDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(articleUrl) {
        if (articleUrl.isBlank()) {
            coroutineScope.launch {
                isLoading = false
                hasError = true
                errorMessage = "文章链接为空"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (articleTitle.isNotBlank()) articleTitle else "文章详情",
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    if (articleId != null) {
                        IconButton(onClick = { showCollectDialog = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_collect),
                                contentDescription = "收藏"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (hasError && articleUrl.isBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                if (isLoading) {
                    LinearProgressIndicator(
                        progress = { loadingProgress / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = Success,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    databaseEnabled = true
                                    loadWithOverviewMode = true
                                    useWideViewPort = true
                                    builtInZoomControls = true
                                    displayZoomControls = false
                                    setSupportZoom(true)
                                }

                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        coroutineScope.launch {
                                            isLoading = false
                                            hasError = false
                                            loadingProgress = 100
                                        }
                                    }

                                    override fun onReceivedError(
                                        view: WebView?,
                                        request: WebResourceRequest?,
                                        error: WebResourceError?
                                    ) {
                                        super.onReceivedError(view, request, error)
                                        coroutineScope.launch {
                                            isLoading = false
                                            hasError = true
                                            errorMessage = "加载失败：${error?.description ?: "未知错误"}"
                                        }
                                    }
                                }

                                webChromeClient = object : WebChromeClient() {
                                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                        super.onProgressChanged(view, newProgress)
                                        coroutineScope.launch {
                                            loadingProgress = newProgress
                                            if (newProgress == 100) {
                                                isLoading = false
                                            }
                                        }
                                    }
                                }

                                if (articleUrl.isNotBlank()) {
                                    loadUrl(articleUrl)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (hasError && articleUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCollectDialog && articleId != null) {
        AlertDialog(
            onDismissRequest = { showCollectDialog = false },
            title = { Text("收藏文章") },
            text = { Text("确定要收藏这篇文章吗？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCollectDialog = false
                        onCollectClick(articleId)
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCollectDialog = false }
                ) {
                    Text("取消")
                }
            }
        )
    }

    if (showUncollectDialog && articleId != null) {
        AlertDialog(
            onDismissRequest = { showUncollectDialog = false },
            title = { Text("取消收藏") },
            text = { Text("确定要取消收藏这篇文章吗？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showUncollectDialog = false
                        onUncollectClick(articleId)
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showUncollectDialog = false }
                ) {
                    Text("取消")
                }
            }
        )
    }
}

