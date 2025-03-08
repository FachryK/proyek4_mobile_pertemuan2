package com.proyek.jtk.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Profile : Screen("profile")
}
