package com.abdullahhalis.expensetracker.ui.screen.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.abdullahhalis.expensetracker.ui.theme.ExpenseTrackerTheme
import com.abdullahhalis.expensetracker.ui.utils.MyCategory
import com.abdullahhalis.expensetracker.ui.utils.toFormattedDate
import com.abdullahhalis.expensetracker.ui.utils.toRupiah
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailExpenseScreen(
    expenseId: Long,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: DetailExpenseViewModel = hiltViewModel(),
) {

    LaunchedEffect(expenseId) {
        viewModel.loadExpense(expenseId)
    }

    val expense by viewModel.expense.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.dateInMillis
    )
    val scrollState = rememberScrollState()

    expense?.let { data ->
        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    "Expense Details", fontWeight = FontWeight.Bold
                )
            }, navigationIcon = {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "back"
                    )
                }
            }, actions = {
                IconButton(
                    onClick = {
                        if (isEdit) viewModel.cancelUpdate()
                        isEdit = !isEdit
                    }) {
                    if (isEdit) {
                        Icon(
                            imageVector = Icons.Rounded.Cancel,
                            contentDescription = "cancel"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Edit, contentDescription = "edit"
                        )
                    }
                }
            })
        }, bottomBar = {
            if (isEdit) {
                Button(
                    onClick = {
                        viewModel.updateExpense {
                            isEdit = false
                        }
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
                            imageVector = Icons.Rounded.Edit, contentDescription = "update"
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Update")
                    }
                }
            } else {
                Button(
                    onClick = { showDeleteDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
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
                            imageVector = Icons.Rounded.Delete, contentDescription = "delete"
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Delete Expense")
                    }
                }
            }
        }) { contentPadding ->
            if (showDeleteDialog) {
                BasicAlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    modifier = modifier.padding(contentPadding)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "delete",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(bottom = 16.dp)
                            )
                            Text(
                                "Delete Expense",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                "Are you sure you want to delete this expense? this action cannot be undone.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 24.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    onClick = { showDeleteDialog = false },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel")
                                }
                                Button(
                                    onClick = {
                                        viewModel.deleteExpense(data.id) {
                                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                                "snackbar_message",
                                                "Expense deleted successfully"
                                            )
                                            navController.popBackStack()
                                        }
                                        showDeleteDialog = false
                                    }, colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError
                                    ), modifier = Modifier.weight(1f)
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
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

            ExpenseDetailsContent(
                scrollState = scrollState,
                isEdit = isEdit,
                title = uiState.title,
                amount = uiState.amount,
                selectedCategory = uiState.category,
                dateInMillis = uiState.dateInMillis,
                note = uiState.note,
                amountError = uiState.amountError,
                titleError = uiState.titleError,
                onDateClick = { showDatePicker = true },
                onAmountChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        viewModel.onAmountChange(newValue)
                    }
                },
                onTitleChange = viewModel::onTitleChange,
                onNoteChange = viewModel::onNoteChange,
                onCategorySelected = viewModel::onCategoryChange,
                modifier = modifier
                    .verticalScroll(scrollState)
                    .padding(contentPadding)
                    .padding(16.dp)
                    .imePadding()
            )
        }
    }
}

@Composable
fun ExpenseDetailsContent(
    scrollState: ScrollState,
    isEdit: Boolean,
    title: String,
    amount: String,
    selectedCategory: MyCategory,
    dateInMillis: Long,
    note: String,
    amountError: String?,
    titleError: String?,
    onAmountChange: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onCategorySelected: (MyCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .size(84.dp)
                .clip(CircleShape)
                .background(selectedCategory.color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = selectedCategory.icon,
                contentDescription = "category",
                tint = selectedCategory.color,
                modifier = Modifier.size(48.dp)
            )
        }
        if (isEdit) {
            TextField(
                value = amount,
                isError = amountError != null,
                onValueChange = onAmountChange,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                supportingText = {
                    amountError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
        } else {
            Text(
                text = amount.toDoubleOrNull()?.toRupiah() ?: "Rp 0",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
        }
        if (isEdit) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                isError = titleError != null,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center
                ),
                supportingText = {
                    titleError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 4.dp)
            )
        } else {
            Text(
                text = title, style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ), modifier = Modifier.padding(bottom = 20.dp)
            )
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "date",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        "Date",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    dateInMillis.toFormattedDate(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = if (!isEdit) Modifier
                    else Modifier
                        .border(
                            1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(1.dp)
                        )
                        .padding(8.dp)
                        .clickable { onDateClick() }
                )
                if (isEdit) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "date",
                        modifier = Modifier
                            .clickable { onDateClick() }
                            .padding(start = 8.dp))
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Category,
                        contentDescription = "category",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        "Category",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
                AssistChip(
                    onClick = {}, enabled = false, label = {
                        Text(
                            selectedCategory.label,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = selectedCategory.color
                        )
                    }, colors = AssistChipDefaults.assistChipColors().copy(
                        disabledContainerColor = selectedCategory.color.copy(0.2f),
                    )
                )
            }
        }
        if (isEdit) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 4.dp, top = 4.dp)
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
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 20.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Notes,
                contentDescription = "note",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Notes", color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            readOnly = !isEdit,
            enabled = isEdit,
            minLines = 3,
            maxLines = 5,
            placeholder = {
                Text(
                    "No notes added",
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Transparent,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant

            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        scope.launch {
                            delay(500)
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    }
                }
        )
    }
}

@Preview
@Composable
private fun DetailExpenseScreenPrev() {
    ExpenseTrackerTheme(
        darkTheme = false
    ) {
        Scaffold { padding ->
            ExpenseDetailsContent(
                rememberScrollState(),
                false,
                "Nasi Padang",
                "15000",
                MyCategory.FOOD,
                1772320216275L,
                "",
                null,
                null,
                {},
                {},
                {},
                {},
                {},
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
        }
    }
}