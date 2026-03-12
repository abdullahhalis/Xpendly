package com.abdullahhalis.expensetracker.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

enum class ThemeOption {
    LIGHT,
    DARK,
    AUTO;

    @Composable
    fun isDarkTheme(): Boolean = when (this) {
        LIGHT -> false
        DARK -> true
        AUTO -> isSystemInDarkTheme()
    }
}