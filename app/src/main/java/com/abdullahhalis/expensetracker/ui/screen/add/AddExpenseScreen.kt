package com.abdullahhalis.expensetracker.ui.screen.add

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdullahhalis.expensetracker.ui.theme.ExpenseTrackerTheme
import com.abdullahhalis.expensetracker.ui.utils.MyCategory
import com.abdullahhalis.expensetracker.ui.utils.toFormattedDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.dateInMillis
    )

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add Expense",
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
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.saveExpense(
                        onSuccess = {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("snackbar_message", "Expense added successfully")
                            navController.popBackStack()
                        }
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "save"
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Save Expense")
                }
            }
        }
    ) { contentPadding ->

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false},
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                viewModel.onDateChange(it)
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        AddExpenseContent(
            scrollState = scrollState,
            amountInput = uiState.amount,
            titleInput = uiState.title,
            noteInput = uiState.note,
            selectedDateInMillis = uiState.dateInMillis,
            selectedCategory = uiState.category,
            onTitleChange = viewModel::onTitleChange,
            onNoteChange = viewModel::onNoteChange,
            onAmountChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    viewModel.onAmountChange(newValue)
                }
            },
            onDateClick = { showDatePicker = true },
            onCategorySelected = viewModel::onCategoryChange,
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(contentPadding)
                .padding(16.dp)
                .imePadding()
        )
    }
}

@Composable
fun AddExpenseContent(
    scrollState: ScrollState,
    amountInput: String,
    titleInput: String,
    noteInput: String,
    selectedDateInMillis: Long,
    selectedCategory: MyCategory,
    onAmountChange: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onCategorySelected: (MyCategory) -> Unit,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
    ) {
        Text(
            "Amount",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = amountInput,
            onValueChange = onAmountChange,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            placeholder = {
                Text(
                    "0",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            leadingIcon = {
                Text("Rp", style = MaterialTheme.typography.titleMedium)
            },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 20.dp, top = 4.dp)
        )
        Text(
            "Title",
            style = MaterialTheme.typography.titleMedium
        )
        OutlinedTextField(
            value = titleInput,
            onValueChange = onTitleChange,
            placeholder = {
                Text(
                    "e.g. Nasi Padang",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Title,
                    contentDescription = "title",
                    tint = LocalContentColor.current.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 20.dp, top = 4.dp)
        )
        Text(
            "Date",
            style = MaterialTheme.typography.titleMedium
        )
        OutlinedTextField(
            value = selectedDateInMillis.toFormattedDate(),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = "date",
                    modifier = Modifier.clickable{ onDateClick() }
                )
            },
            modifier = Modifier.fillMaxWidth()
                .clickable{ onDateClick() }
                .padding(bottom = 20.dp, top = 4.dp)
        )
        Text(
            "Category",
            style = MaterialTheme.typography.titleMedium
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 20.dp, top = 4.dp)
        ) {
            MyCategory.entries.forEach { category ->
                val isSelected = category == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    label = {
                        Text(category.label)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = "category"
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = category.color.copy(alpha = 0.2f),
                        selectedLabelColor = category.color,
                        selectedLeadingIconColor = category.color,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = MaterialTheme.colorScheme.outlineVariant,
                        borderWidth = 1.dp
                    )
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Note", style = MaterialTheme.typography.titleMedium)
            Text(
                "Optional",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        OutlinedTextField(
            value = noteInput,
            onValueChange = onNoteChange,
            minLines = 3,
            maxLines = 5,
            placeholder = {
                Text(
                    "Add a note ...",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Notes,
                    contentDescription = "title",
                    tint = LocalContentColor.current.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier.fillMaxWidth()
                .onFocusChanged{ focusState ->
                    if (focusState.isFocused) {
                        scope.launch {
                            delay(500)
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    }
                }
                .padding(bottom = 20.dp, top = 4.dp)
        )
    }
}

@Preview
@Composable
private fun AddExpensePrev() {
    ExpenseTrackerTheme(
        darkTheme = false
    ) {
        AddExpenseScreen(
            rememberNavController()
        )
    }
}