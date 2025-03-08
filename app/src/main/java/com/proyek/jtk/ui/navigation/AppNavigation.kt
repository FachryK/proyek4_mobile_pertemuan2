package com.proyek.jtk.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.proyek.jtk.ui.SplashScreenContent
import com.proyek.jtk.api.ApiService
import com.proyek.jtk.ui.screen.home.HomeScreen
import com.proyek.jtk.ui.screen.profile.ProfileScreen
import com.proyek.jtk.ui.screen.detail.DetailScreen
import com.proyek.jtk.ui.screen.detail.DetailViewModel
import com.proyek.jtk.ui.screen.detail.DetailViewModelFactory
import com.proyek.jtk.ui.screen.profile.ProfileViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != "detail/{schoolId}" && currentRoute != Screen.Splash.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreenContent(navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.Profile.route) {
                val profileViewModel: ProfileViewModel = viewModel()
                ProfileScreen(viewModel = profileViewModel)
            }
            composable(
                "detail/{schoolId}",
                arguments = listOf(navArgument("schoolId") { type = NavType.StringType })
            ) { backStackEntry ->
                val schoolId = backStackEntry.arguments?.getString("schoolId")
                schoolId?.let {
                    val detailViewModel: DetailViewModel = viewModel(factory = DetailViewModelFactory(ApiService.create()))
                    DetailScreen(backStackEntry = backStackEntry, viewModel = detailViewModel)
                }
            }
        }
    }
}



@Composable
private fun BottomBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem("Home", Icons.Default.Home, Screen.Home),
            NavigationItem("Profile", Icons.Default.AccountCircle, Screen.Profile)
        )

        navigationItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}