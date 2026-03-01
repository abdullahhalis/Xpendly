package com.abdullahhalis.expensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abdullahhalis.expensetracker.ui.screen.AddExpenseScreen
import com.abdullahhalis.expensetracker.ui.screen.HomeScreen

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object AddExpense: Screen("add_expense")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.AddExpense.route) {
            AddExpenseScreen(navController)
        }
    }
}