package com.gradle.aicodeapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.gradle.aicodeapp.ui.components.BottomNavigationBar
import com.gradle.aicodeapp.ui.pages.HomePage
import com.gradle.aicodeapp.ui.pages.SquarePage
import com.gradle.aicodeapp.ui.pages.ProjectPage
import com.gradle.aicodeapp.ui.pages.NavigationPage
import com.gradle.aicodeapp.ui.pages.MinePage
import com.gradle.aicodeapp.ui.theme.AiCodeAppTheme
import com.gradle.aicodeapp.ui.viewmodel.HomeViewModel
import com.gradle.aicodeapp.ui.viewmodel.SquareViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiCodeAppTheme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    var selectedItem by rememberSaveable {
        androidx.compose.runtime.mutableStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { paddingValues ->
        when (selectedItem) {
            0 -> {
                val viewModel: HomeViewModel = viewModel()
                HomePage(viewModel, paddingValues)
            }
            1 -> {
                val viewModel: SquareViewModel = viewModel()
                SquarePage(viewModel, paddingValues)
            }
            2 -> ProjectPage()
            3 -> NavigationPage()
            4 -> MinePage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    AiCodeAppTheme {
        // 直接预览HomePage，不使用ViewModel
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
                text = "点击按钮发起网络请求",
                textAlign = TextAlign.Center
            )
        }
    }
}