package com.abdullahhalis.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.abdullahhalis.expensetracker.data.local.ThemePreference
import com.abdullahhalis.expensetracker.ui.navigation.AppNavigation
import com.abdullahhalis.expensetracker.ui.theme.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreference: ThemePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val initialTheme = runBlocking {
            themePreference.themeOption.first()
        }
        setContent {
            val themeOption = themePreference.themeOption
                .collectAsStateWithLifecycle(initialValue = initialTheme)

            ExpenseTrackerTheme(
                darkTheme = themeOption.value.isDarkTheme()
            ) {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}