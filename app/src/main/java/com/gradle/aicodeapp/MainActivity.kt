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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.ui.components.BottomNavigationBar
import com.gradle.aicodeapp.ui.pages.HomePage
import com.gradle.aicodeapp.ui.pages.LoginPage
import com.gradle.aicodeapp.ui.pages.MinePage
import com.gradle.aicodeapp.ui.pages.NavigationPage
import com.gradle.aicodeapp.ui.pages.ProjectPage
import com.gradle.aicodeapp.ui.pages.RegisterPage
import com.gradle.aicodeapp.ui.pages.SquarePage
import com.gradle.aicodeapp.ui.theme.AiCodeAppTheme
import com.gradle.aicodeapp.ui.viewmodel.HomeViewModel
import com.gradle.aicodeapp.ui.viewmodel.SquareViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiCodeAppTheme {
                MainScreen(userManager)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(userManager: UserManager) {
    var isLoggedIn by rememberSaveable { mutableStateOf(userManager.isLoggedIn()) }
    var currentPage by rememberSaveable { mutableStateOf("login") }
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    when {
        !isLoggedIn -> {
            when (currentPage) {
                "login" -> {
                    LoginPage(
                        onLoginSuccess = {
                            isLoggedIn = true
                            currentPage = "home"
                        },
                        onNavigateToRegister = {
                            currentPage = "register"
                        }
                    )
                }
                "register" -> {
                    RegisterPage(
                        onRegisterSuccess = {
                            isLoggedIn = true
                            currentPage = "home"
                        },
                        onNavigateToLogin = {
                            currentPage = "login"
                        }
                    )
                }
            }
        }
        else -> {
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
                    4 -> MinePage(
                        onLogout = {
                            userManager.clearUserInfo()
                            isLoggedIn = false
                            currentPage = "login"
                            selectedItem = 0
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    AiCodeAppTheme {
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