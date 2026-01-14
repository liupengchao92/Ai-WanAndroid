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
import androidx.navigation.NavHostController
import com.gradle.aicodeapp.ui.components.BottomNavigationBar

@Composable
fun MainTabNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
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
    ) { paddingValues ->
        when (selectedItem) {
            0 -> {
                navController.navigate(NavigationRoutes.HOME) {
                    popUpTo(NavigationRoutes.HOME) { inclusive = true }
                }
            }
            1 -> {
                navController.navigate(NavigationRoutes.SQUARE) {
                    popUpTo(NavigationRoutes.SQUARE) { inclusive = true }
                }
            }
            2 -> {
                navController.navigate(NavigationRoutes.PROJECT) {
                    popUpTo(NavigationRoutes.PROJECT) { inclusive = true }
                }
            }
            3 -> {
                navController.navigate(NavigationRoutes.NAVIGATION) {
                    popUpTo(NavigationRoutes.NAVIGATION) { inclusive = true }
                }
            }
            4 -> {
                navController.navigate(NavigationRoutes.MINE) {
                    popUpTo(NavigationRoutes.MINE) { inclusive = true }
                }
            }
        }
    }
}
