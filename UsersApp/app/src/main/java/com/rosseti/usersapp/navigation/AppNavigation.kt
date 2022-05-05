package com.rosseti.usersapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rosseti.usersapp.ui.home.HomeScreen

@ExperimentalComposeUiApi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.MainScreen.name
    ) {
        composable(AppScreens.MainScreen.name) {
            HomeScreen(navController = navController)
        }

    }
}