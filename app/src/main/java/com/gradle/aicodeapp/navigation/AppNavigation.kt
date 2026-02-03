package com.gradle.aicodeapp.navigation

import android.text.TextUtils
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
import com.gradle.aicodeapp.network.model.PopularWenda
import com.gradle.aicodeapp.ui.components.BottomNavigationBar
import com.gradle.aicodeapp.ui.components.GlobalErrorHandler
import com.gradle.aicodeapp.ui.pages.ArticleDetailPage
import com.gradle.aicodeapp.ui.pages.CoinPage
import com.gradle.aicodeapp.ui.pages.CollectAddPage
import com.gradle.aicodeapp.ui.pages.CollectEditPage
import com.gradle.aicodeapp.ui.pages.CollectPage
import com.gradle.aicodeapp.ui.pages.ColumnListPage
import com.gradle.aicodeapp.ui.pages.HomePage
import com.gradle.aicodeapp.ui.pages.LoginPage
import com.gradle.aicodeapp.ui.pages.MessagePage
import com.gradle.aicodeapp.ui.pages.MinePage
import com.gradle.aicodeapp.ui.pages.NavigationPage
import com.gradle.aicodeapp.ui.pages.ProjectPage
import com.gradle.aicodeapp.ui.pages.RegisterPage
import com.gradle.aicodeapp.ui.pages.RouteListPage
import com.gradle.aicodeapp.ui.pages.SearchPage
import com.gradle.aicodeapp.ui.pages.SettingsPage
import com.gradle.aicodeapp.ui.pages.SquarePage
import com.gradle.aicodeapp.ui.pages.TodoFormPage
import com.gradle.aicodeapp.ui.pages.TodoPage
import com.gradle.aicodeapp.ui.pages.WendaDetailPage
import com.gradle.aicodeapp.ui.pages.WendaListPage
import com.gradle.aicodeapp.ui.pages.WxArticleListPage
import com.gradle.aicodeapp.ui.viewmodel.CollectViewModel
import com.gradle.aicodeapp.ui.viewmodel.HomeViewModel
import com.gradle.aicodeapp.ui.viewmodel.ProjectViewModel
import com.gradle.aicodeapp.ui.viewmodel.SearchViewModel
import com.gradle.aicodeapp.ui.viewmodel.SquareViewModel
import com.gradle.aicodeapp.ui.viewmodel.TodoViewModel
import com.gradle.aicodeapp.ui.viewmodel.WendaListViewModel
import com.gradle.aicodeapp.utils.JsonUtils
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
    modifier: Modifier = Modifier,
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
                            val encodedUrl =
                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle =
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    onWendaClick = { wenda ->
                        val wendaJson = JsonUtils.toJson(wenda)?.let {
                            URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                        } ?: ""
                        navController.navigate("${NavigationRoutes.WENDA_DETAIL}/$wendaJson")
                    },
                    onNavigateToWendaList = {
                        navController.navigate(NavigationRoutes.WENDA_LIST)
                    },
                    onNavigateToColumnList = {
                        navController.navigate(NavigationRoutes.COLUMN_LIST)
                    },
                    onNavigateToRouteList = {
                        navController.navigate(NavigationRoutes.ROUTE_LIST)
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
                            val encodedUrl =
                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle =
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    onSearchClick = {
                        navController.navigate(NavigationRoutes.SEARCH)
                    },
                    onWxAccountClick = { accountId, accountName ->
                        val encodedName =
                            URLEncoder.encode(accountName, StandardCharsets.UTF_8.toString())
                        navController.navigate("${NavigationRoutes.WX_ARTICLE_LIST}/$accountId?${NavigationArguments.WX_ACCOUNT_NAME}=$encodedName")
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
                            val encodedUrl =
                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle =
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
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
                            val encodedUrl =
                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle =
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.MINE) {
                MinePage(
                    userManager = userManager,
                    onNavigateToCollect = {
                        navController.navigate(NavigationRoutes.COLLECT)
                    },
                    onNavigateToCoin = {
                        navController.navigate(NavigationRoutes.COIN)
                    },
                    onNavigateToTodo = {
                        navController.navigate(NavigationRoutes.TODO)
                    },
                    onNavigateToSettings = {
                        navController.navigate(NavigationRoutes.SETTINGS)
                    },
                    onNavigateToMessage = {
                        navController.navigate(NavigationRoutes.MESSAGE)
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.MESSAGE) {
                MessagePage(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNavigateToLogin = {
                        navController.navigate(NavigationRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
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
                            val encodedUrl =
                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle =
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.COIN) {
                CoinPage(
                    onNavigateToLogin = {
                        navController.navigate(NavigationRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.SETTINGS) {
                SettingsPage(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onLogout = {
                        userManager.clearUserInfo()
                        navController.navigate(NavigationRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.TODO) {
                val viewModel: TodoViewModel = hiltViewModel()
                TodoPage(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToEdit = { todoId ->
                        navController.navigate("${NavigationRoutes.TODO_FORM}/$todoId")
                    },
                    onNavigateToAdd = {
                        navController.navigate(NavigationRoutes.TODO_FORM)
                    },
                    paddingValues = paddingValues
                )
            }

            composable(
                route = "${NavigationRoutes.TODO_FORM}/{${NavigationArguments.TODO_ID}}",
                arguments = listOf(
                    navArgument(NavigationArguments.TODO_ID) { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val todoId = backStackEntry.arguments?.getInt(NavigationArguments.TODO_ID)
                // 使用与TodoPage相同的ViewModel实例
                val viewModel: TodoViewModel =
                    hiltViewModel(navController.getBackStackEntry(NavigationRoutes.TODO))
                TodoFormPage(
                    todoId = todoId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    paddingValues = paddingValues
                )
            }

            composable(NavigationRoutes.TODO_FORM) {
                // 使用与TodoPage相同的ViewModel实例
                val viewModel: TodoViewModel =
                    hiltViewModel(navController.getBackStackEntry(NavigationRoutes.TODO))
                TodoFormPage(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
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
                val articleId =
                    backStackEntry.arguments?.getInt(NavigationArguments.ARTICLE_ID) ?: 0
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
                            val encodedUrl =
                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle =
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(NavigationRoutes.WENDA_LIST) {
                val viewModel: WendaListViewModel = hiltViewModel()
                WendaListPage(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onWendaClick = { wenda ->
                        val wendaJson = JsonUtils.toJson(wenda)?.let {
                            URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                        } ?: ""
                        navController.navigate("${NavigationRoutes.WENDA_DETAIL}/$wendaJson")
                    }
                )
            }

            composable(
                route = "${NavigationRoutes.WENDA_DETAIL}/{${NavigationArguments.WENDA_JSON}}",
                arguments = listOf(
                    navArgument(NavigationArguments.WENDA_JSON) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedWendaJson = backStackEntry.arguments?.getString(NavigationArguments.WENDA_JSON) ?: ""
                val wendaJson = try {
                    URLDecoder.decode(encodedWendaJson, StandardCharsets.UTF_8.toString())
                } catch (e: Exception) {
                    encodedWendaJson
                }
                val wenda: PopularWenda? = JsonUtils.fromJson(wendaJson, PopularWenda::class.java)
                WendaDetailPage(
                    wenda = wenda,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNavigateToLogin = {
                        navController.navigate(NavigationRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavigationRoutes.COLUMN_LIST) {
                ColumnListPage(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onColumnClick = { url, title ->
                        if (url.isNotBlank()) {
                            val encodedUrl =
                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle =
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    }
                )
            }

            composable(NavigationRoutes.ROUTE_LIST) {
                RouteListPage(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRouteClick = { url, title ->
                        if (url.isNotBlank()) {
                            val encodedUrl =
                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle =
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    }
                )
            }

            composable(
                route = "${NavigationRoutes.WX_ARTICLE_LIST}/{${NavigationArguments.WX_ACCOUNT_ID}}?${NavigationArguments.WX_ACCOUNT_NAME}={${NavigationArguments.WX_ACCOUNT_NAME}}",
                arguments = listOf(
                    navArgument(NavigationArguments.WX_ACCOUNT_ID) { type = NavType.IntType },
                    navArgument(NavigationArguments.WX_ACCOUNT_NAME) {
                        type = NavType.StringType; nullable = true
                    }
                )
            ) { backStackEntry ->
                val accountId =
                    backStackEntry.arguments?.getInt(NavigationArguments.WX_ACCOUNT_ID) ?: 0
                val accountName = try {
                    val encodedName =
                        backStackEntry.arguments?.getString(NavigationArguments.WX_ACCOUNT_NAME)
                            ?: ""
                    URLDecoder.decode(encodedName, StandardCharsets.UTF_8.toString())
                } catch (e: Exception) {
                    "公众号文章"
                }
                WxArticleListPage(
                    accountId = accountId,
                    accountName = accountName,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onArticleClick = { url, title ->
                        if (url.isNotBlank()) {
                            val encodedUrl =
                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            val encodedTitle =
                                URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
                            navController.navigate("${NavigationRoutes.ARTICLE_DETAIL}/$encodedUrl?${NavigationArguments.ARTICLE_TITLE}=$encodedTitle")
                        }
                    }
                )
            }

            composable(
                route = "${NavigationRoutes.ARTICLE_DETAIL}/{${NavigationArguments.ARTICLE_URL}}?${NavigationArguments.ARTICLE_TITLE}={${NavigationArguments.ARTICLE_TITLE}}",
                arguments = listOf(
                    navArgument(NavigationArguments.ARTICLE_URL) { type = NavType.StringType },
                    navArgument(NavigationArguments.ARTICLE_TITLE) {
                        type = NavType.StringType; nullable = true
                    }
                )
            ) { backStackEntry ->
                val encodedUrl =
                    backStackEntry.arguments?.getString(NavigationArguments.ARTICLE_URL) ?: ""
                val articleUrl = try {
                    URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
                } catch (e: Exception) {
                    encodedUrl
                }
                val encodedTitle =
                    backStackEntry.arguments?.getString(NavigationArguments.ARTICLE_TITLE) ?: ""
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
