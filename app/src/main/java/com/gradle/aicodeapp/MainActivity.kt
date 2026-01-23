package com.gradle.aicodeapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.data.preferences.SettingsDataStore
import com.gradle.aicodeapp.navigation.AppNavigation
import com.gradle.aicodeapp.ui.components.BottomNavigationBar
import com.gradle.aicodeapp.ui.pages.HomePage
import com.gradle.aicodeapp.ui.pages.ArticleDetailPage
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
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(userManager, settingsDataStore)
        }
    }
}

@Composable
fun MainScreen(userManager: UserManager, settingsDataStore: SettingsDataStore) {
    val darkMode by settingsDataStore.darkMode.collectAsState(initial = SettingsDataStore.DARK_MODE_SYSTEM)
    val isDarkTheme = when (darkMode) {
        SettingsDataStore.DARK_MODE_DARK -> true
        SettingsDataStore.DARK_MODE_LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    val navController = rememberNavController()
    AiCodeAppTheme(darkTheme = isDarkTheme) {
        AppNavigation(
            navController = navController,
            userManager = userManager
        )
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
