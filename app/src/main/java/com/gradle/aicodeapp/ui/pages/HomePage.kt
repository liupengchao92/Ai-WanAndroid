package com.gradle.aicodeapp.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gradle.aicodeapp.network.repository.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun HomePage(
    networkRepository: NetworkRepository
) {
    var requestResult by remember {
        mutableStateOf("点击按钮发起网络请求")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    fun makeNetworkRequest() {
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            val result = networkRepository.getBanners()
            CoroutineScope(Dispatchers.Main).launch {
                requestResult = if (result.isSuccess) {
                    val response = result.getOrNull()
                    if (response?.isSuccess() == true) {
                        "请求成功: 获取到 ${response.data?.size ?: 0} 个Banner"
                    } else {
                        "API错误: ${response?.errorMsg ?: "未知错误"} (code: ${response?.errorCode ?: -1})"
                    }
                } else {
                    "网络错误: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                }
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "首页",
            textAlign = TextAlign.Center
        )
        Text(
            text = requestResult,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = ::makeNetworkRequest,
            enabled = !isLoading
        ) {
            Text(
                text = if (isLoading) "请求中..." else "发起网络请求"
            )
        }
    }
}
