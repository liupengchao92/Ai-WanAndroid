package com.gradle.aicodeapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.gradle.aicodeapp.ui.components.GlobalErrorHandler
import com.gradle.aicodeapp.ui.pages.ArticleDetailPage
import com.gradle.aicodeapp.ui.pages.CollectAddPage
import com.gradle.aicodeapp.ui.pages.CollectEditPage
import com.gradle.aicodeapp.ui.pages.CollectPage
import com.gradle.aicodeapp.ui.pages.HomePage
import com.gradle.aicodeapp.ui.pages.LoginPage
import com.gradle.aicodeapp.ui.pages.MinePage
import com.gradle.aicodeapp.ui.pages.NavigationPage
import com.gradle.aicodeapp.ui.pages.ProjectPage
import com.gradle.aicodeapp.ui.pages.RegisterPage
import com.gradle.aicodeapp.ui.pages.SearchPage
import com.gradle.aicodeapp.ui.pages.SquarePage
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import com.gradle.aicodeapp.ui.viewmodel.HomeViewModel
import com.gradle.aicodeapp.ui.viewmodel.ProjectViewModel
import com.gradle.aicodeapp.ui.viewmodel.SearchViewModel
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

    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            NavigationRoutes.HOME -> selectedItem = 0
            NavigationRoutes.SQUARE -> selectedItem = 1
            NavigationRoutes.PROJECT -> selectedItem = 2
            NavigationRoutes.NAVIGATION -> selectedItem = 3
            NavigationRoutes.MINE -> selectedItem = 4
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItem = selectedItem,
                    onItemSelected = { index ->
                        when (index) {
                            0 -> navController.navigate(NavigationRoutes.HOME) {
                                launchSingleTop = true
                            }
                            1 -> navController.navigate(NavigationRoutes.SQUARE) {
                                launchSingleTop = true
                            }
                            2 -> navController.navigate(NavigationRoutes.PROJECT) {
                                launchSingleTop = true
                            }
                            3 -> navController.navigate(NavigationRoutes.NAVIGATION) {
                                launchSingleTop = true
                            }
                            4 -> navController.navigate(NavigationRoutes.MINE) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        GlobalErrorHandler(
            onNavigateToLogin = {
                navController.navigate(NavigationRoutes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )

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
                    onArticleClick = { url, title ->
                        if (url.isNotBlank()) {
                            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.SQUARE) {
                val viewModel: SquareViewModel = hiltViewModel()
                SquarePage(
                    viewModel = viewModel,
                    onArticleClick = { url, title ->
                        if (url.isNotBlank()) {
                            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    onSearchClick = {
                        navController.navigate(NavigationRoutes.SEARCH)
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.PROJECT) {
                val viewModel: ProjectViewModel = hiltViewModel()
                ProjectPage(
                    viewModel = viewModel,
                    onArticleClick = { url, title ->
                        if (url.isNotBlank()) {
                            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.NAVIGATION) {
                NavigationPage(
                    onArticleClick = { url, title ->
                        if (url.isNotBlank()) {
                            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.MINE) {
                MinePage(
                    userManager = userManager,
                    onLogout = {
                        userManager.clearUserInfo()
                        navController.navigate(NavigationRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToCollect = {
                        navController.navigate(NavigationRoutes.COLLECT)
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.COLLECT) {
                val viewModel: CollectViewModel = hiltViewModel()
                CollectPage(
                    viewModel = viewModel,
                    navController = navController,
                    onArticleClick = { url, title ->
                        if (url.isNotBlank()) {
                            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.COLLECT_ADD) {
                val viewModel: CollectViewModel = hiltViewModel()
                CollectAddPage(
                    onBackClick = { navController.popBackStack() },
                    onSubmit = { title, author, link ->
                        viewModel.collectOutsideArticle(title, author, link)
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "${NavigationRoutes.COLLECT_EDIT}/{${NavigationArguments.ARTICLE_ID}}?title={title}&author={author}&link={link}",
                arguments = listOf(
                    navArgument(NavigationArguments.ARTICLE_ID) { type = NavType.IntType },
                    navArgument("title") { type = NavType.StringType; nullable = true },
                    navArgument("author") { type = NavType.StringType; nullable = true },
                    navArgument("link") { type = NavType.StringType; nullable = true }
                )
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getInt(NavigationArguments.ARTICLE_ID) ?: 0
                val title = try {
                    val encodedTitle = backStackEntry.arguments?.getString("title") ?: ""
                    URLDecoder.decode(encodedTitle, StandardCharsets.UTF_8.toString())
                } catch (e: Exception) {
                    ""
                }
                val author = try {
                    val encodedAuthor = backStackEntry.arguments?.getString("author") ?: ""
                    URLDecoder.decode(encodedAuthor, StandardCharsets.UTF_8.toString())
                } catch (e: Exception) {
                    ""
                }
                val link = try {
                    val encodedLink = backStackEntry.arguments?.getString("link") ?: ""
                    URLDecoder.decode(encodedLink, StandardCharsets.UTF_8.toString())
                } catch (e: Exception) {
                    ""
                }
                val viewModel: CollectViewModel = hiltViewModel()
                CollectEditPage(
                    articleId = articleId,
                    initialTitle = title,
                    initialAuthor = author,
                    initialLink = link,
                    onBackClick = { navController.popBackStack() },
                    onSubmit = { id, newTitle, newAuthor, newLink ->
                        viewModel.updateCollectArticle(id, newTitle, newAuthor, newLink)
                        navController.popBackStack()
                    }
                )
            }

            composable(NavigationRoutes.SEARCH) {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchPage(
                    viewModel = viewModel,
                    onArticleClick = { url, title ->
                        if (url.isNotBlank()) {
                            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "${NavigationRoutes.ARTICLE_DETAIL}/{${NavigationArguments.ARTICLE_URL}}?${NavigationArguments.ARTICLE_TITLE}={${NavigationArguments.ARTICLE_TITLE}}",
                arguments = listOf(
                    navArgument(NavigationArguments.ARTICLE_URL) { type = NavType.StringType },
                    navArgument(NavigationArguments.ARTICLE_TITLE) { type = NavType.StringType; nullable = true }
                )
            ) { backStackEntry ->
                val encodedUrl = backStackEntry.arguments?.getString(NavigationArguments.ARTICLE_URL) ?: ""
                val articleUrl = try {
                    URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
                } catch (e: Exception) {
                    encodedUrl
                }
                val encodedTitle = backStackEntry.arguments?.getString(NavigationArguments.ARTICLE_TITLE) ?: ""
                val articleTitle = try {
                    URLDecoder.decode(encodedTitle, StandardCharsets.UTF_8.toString())
                } catch (e: Exception) {
                    encodedTitle
                }
                val viewModel: CollectViewModel = hiltViewModel()
                ArticleDetailPage(
                    articleUrl = articleUrl,
                    articleTitle = articleTitle,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onCollectClick = { articleId ->
                        viewModel.collectArticle(articleId)
                    },
                    onUncollectClick = { articleId ->
                        viewModel.uncollectArticle(articleId)
                    }
                )
            }
        }
    }
}
