package com.abdullahhalis.expensetracker.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.abdullahhalis.expensetracker.ui.utils.ThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class ThemePreference @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val themeOption: Flow<ThemeOption> = dataStore.data.map { preferences ->
        val value = preferences[THEME_KEY] ?: ThemeOption.AUTO.name
        ThemeOption.valueOf(value)
    }

    suspend fun setThemeOption(option: ThemeOption) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = option.name
        }
    }

    companion object {
        val THEME_KEY = stringPreferencesKey("theme_option")
    }

}