package com.rosseti.usersapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rosseti.usersapp.ui.details.UserDetailsScreen
import com.rosseti.usersapp.ui.details.UserDetailsViewModel
import com.rosseti.usersapp.ui.home.HomeScreen
import com.rosseti.usersapp.ui.home.HomeViewModel

@ExperimentalComposeUiApi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.MainScreen.name
    ) {
        composable(AppScreens.MainScreen.name) {
            val viewModel: HomeViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                viewModel.fetchUsers()
            }
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(AppScreens.UserDetailsScreen.name) {
            val viewModel: UserDetailsViewModel = hiltViewModel()
            UserDetailsScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            AppScreens.UserDetailsScreen.name + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel: UserDetailsViewModel = hiltViewModel()
            val userId = backStackEntry.getString("userId")
            LaunchedEffect(Unit) {
                viewModel.fetchUsersById(userId)
            }
            UserDetailsScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                userId = userId
            )
        }
    }
}

fun NavBackStackEntry.getString(value: String) = this.arguments?.getString(value) ?: ""