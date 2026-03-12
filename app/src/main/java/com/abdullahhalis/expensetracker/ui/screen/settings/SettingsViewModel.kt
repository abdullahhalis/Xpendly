package com.abdullahhalis.expensetracker.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullahhalis.expensetracker.data.local.ThemePreference
import com.abdullahhalis.expensetracker.ui.utils.ThemeOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreference: ThemePreference
): ViewModel() {

    val themeOption = themePreference.themeOption
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeOption.AUTO
        )

    fun setThemeOption(option: ThemeOption) {
        viewModelScope.launch {
            themePreference.setThemeOption(option)
        }
    }
}