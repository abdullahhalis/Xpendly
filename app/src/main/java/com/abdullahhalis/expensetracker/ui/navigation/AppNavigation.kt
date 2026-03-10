package com.abdullahhalis.expensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.abdullahhalis.expensetracker.ui.screen.add.AddExpenseScreen
import com.abdullahhalis.expensetracker.ui.screen.detail.DetailExpenseScreen
import com.abdullahhalis.expensetracker.ui.screen.home.HomeScreen

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object AddExpense: Screen("add_expense")
    object Detail: Screen("detail/{expenseId}") {
        fun createRoute(expenseId: Long) = "detail/$expenseId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.AddExpense.route) {
            AddExpenseScreen(navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("expenseId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getLong("expenseId") ?: 0
            DetailExpenseScreen(
                expenseId = expenseId,
                navController = navController
            )
        }
    }
}