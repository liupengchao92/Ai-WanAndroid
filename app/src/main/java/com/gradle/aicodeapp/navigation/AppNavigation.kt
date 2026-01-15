package com.gradle.aicodeapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.gradle.aicodeapp.data.UserManager
import com.gradle.aicodeapp.ui.components.BottomNavigationBar
import com.gradle.aicodeapp.ui.pages.ArticleDetailPage
import com.gradle.aicodeapp.ui.pages.HomePage
import com.gradle.aicodeapp.ui.pages.LoginPage
import com.gradle.aicodeapp.ui.pages.MinePage
import com.gradle.aicodeapp.ui.pages.NavigationPage
import com.gradle.aicodeapp.ui.pages.ProjectPage
import com.gradle.aicodeapp.ui.pages.RegisterPage
import com.gradle.aicodeapp.ui.pages.SquarePage
import com.gradle.aicodeapp.ui.viewmodel.HomeViewModel
import com.gradle.aicodeapp.ui.viewmodel.SquareViewModel
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

private val bottomNavRoutes = listOf(
    NavigationRoutes.HOME,
    NavigationRoutes.SQUARE,
    NavigationRoutes.PROJECT,
    NavigationRoutes.NAVIGATION,
    NavigationRoutes.MINE
)

@Composable
fun AppNavigation(
    navController: NavHostController,
    userManager: UserManager,
    modifier: Modifier = Modifier
) {
    val startDestination = if (userManager.isLoggedIn()) {
        NavigationRoutes.HOME
    } else {
        NavigationRoutes.LOGIN
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavRoutes

    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItem = selectedItem,
                    onItemSelected = { index ->
                        selectedItem = index
                        when (index) {
                            0 -> navController.navigate(NavigationRoutes.HOME) {
                                popUpTo(NavigationRoutes.HOME) { inclusive = true }
                            }
                            1 -> navController.navigate(NavigationRoutes.SQUARE) {
                                popUpTo(NavigationRoutes.SQUARE) { inclusive = true }
                            }
                            2 -> navController.navigate(NavigationRoutes.PROJECT) {
                                popUpTo(NavigationRoutes.PROJECT) { inclusive = true }
                            }
                            3 -> navController.navigate(NavigationRoutes.NAVIGATION) {
                                popUpTo(NavigationRoutes.NAVIGATION) { inclusive = true }
                            }
                            4 -> navController.navigate(NavigationRoutes.MINE) {
                                popUpTo(NavigationRoutes.MINE) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(NavigationRoutes.LOGIN) {
                LoginPage(
                    onLoginSuccess = {
                        selectedItem = 0
                        navController.navigate(NavigationRoutes.HOME) {
                            popUpTo(NavigationRoutes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(NavigationRoutes.REGISTER)
                    }
                )
            }

            composable(NavigationRoutes.REGISTER) {
                RegisterPage(
                    onRegisterSuccess = {
                        selectedItem = 0
                        navController.navigate(NavigationRoutes.HOME) {
                            popUpTo(NavigationRoutes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(NavigationRoutes.HOME) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomePage(
                    viewModel = viewModel,
                    onArticleClick = { url ->
                        if (url.isNotBlank()) {
                            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl")
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.SQUARE) {
                val viewModel: SquareViewModel = hiltViewModel()
                SquarePage(
                    viewModel = viewModel,
                    onArticleClick = { url ->
                        if (url.isNotBlank()) {
                            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl")
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.PROJECT) {
                ProjectPage(paddingValues = paddingValues)
            }

            composable(NavigationRoutes.NAVIGATION) {
                NavigationPage(paddingValues = paddingValues)
            }

            composable(NavigationRoutes.MINE) {
                MinePage(
                    onLogout = {
                        userManager.clearUserInfo()
                        navController.navigate(NavigationRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(
                route = "${NavigationRoutes.ARTICLE_DETAIL}/{${NavigationArguments.ARTICLE_URL}}",
                arguments = listOf(
                    navArgument(NavigationArguments.ARTICLE_URL) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedUrl = backStackEntry.arguments?.getString(NavigationArguments.ARTICLE_URL) ?: ""
                val articleUrl = try {
                    URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
                } catch (e: Exception) {
                    encodedUrl
                }
                ArticleDetailPage(
                    articleUrl = articleUrl,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
