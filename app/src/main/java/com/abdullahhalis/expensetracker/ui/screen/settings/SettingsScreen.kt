package com.abdullahhalis.expensetracker.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.SettingsBrightness
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.abdullahhalis.expensetracker.ui.utils.ThemeOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val themeOption by viewModel.themeOption.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Palette,
                    contentDescription = "theme",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("Theme", style = MaterialTheme.typography.bodyLarge)
            }

            SingleChoiceSegmentedButtonRow {
                ThemeOption.entries.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = themeOption == option,
                        onClick = { viewModel.setThemeOption(option) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ThemeOption.entries.size
                        ),
                        label = {
                            Text(
                                when (option) {
                                    ThemeOption.LIGHT -> "Light"
                                    ThemeOption.DARK -> "Dark"
                                    ThemeOption.AUTO -> "Auto"
                                }
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = when (option) {
                                    ThemeOption.LIGHT -> Icons.Rounded.LightMode
                                    ThemeOption.DARK -> Icons.Rounded.DarkMode
                                    ThemeOption.AUTO -> Icons.Rounded.SettingsBrightness
                                },
                                contentDescription = "theme button"
                            )
                        }
                    )
                }
            }
        }
    }
}