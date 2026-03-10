package com.abdullahhalis.expensetracker.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailExpenseScreen(
    expenseId: Long,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: DetailExpenseViewModel = hiltViewModel()
) {

    LaunchedEffect(expenseId) {
        viewModel.loadExpense(expenseId)
    }

    val expense by viewModel.expense.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }

    expense?.let { data ->
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Expense Details",
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
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "delete"
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Delete Expense")
                    }
                }
            }
        ) { contentPadding ->
            if (showDeleteDialog) {
                BasicAlertDialog(
                    onDismissRequest = { showDeleteDialog = false},
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
                                modifier = Modifier.size(32.dp)
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
                                            navController.previousBackStackEntry
                                                ?.savedStateHandle
                                                ?.set("snackbar_message", "Expense deleted successfully")
                                            navController.popBackStack()
                                        }
                                        showDeleteDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }

            ExpenseDetailsContent(
                title = data.title,
                amount = data.amount,
                selectedCategory = MyCategory.getByLabel(data.category),
                dateInMillis = data.dateInMillis,
                note = data.note,
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun ExpenseDetailsContent(
    title: String,
    amount: Double,
    selectedCategory: MyCategory,
    dateInMillis: Long,
    note: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(selectedCategory.color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = selectedCategory.icon,
                contentDescription = "category",
                tint = selectedCategory.color,
                modifier = Modifier.size(36.dp)
            )
        }
        Text(
            text = amount.toRupiah(),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 12.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
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
                Text(
                    dateInMillis.toFormattedDate(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
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
                    onClick = {},
                    enabled = false,
                    label = {
                        Text(
                            selectedCategory.label,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = selectedCategory.color
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors().copy(
                        disabledContainerColor = selectedCategory.color.copy(0.2f),
                    )
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 20.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Notes,
                contentDescription = "note",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Notes",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        OutlinedTextField(
            value = note,
            onValueChange = {},
            readOnly = true,
            enabled = false,
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
            modifier = Modifier.fillMaxWidth()
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
                "Nasi Padang",
                15000.0,
                MyCategory.FOOD,
                1772320216275L,
                "",
                modifier = Modifier.padding(padding).padding(16.dp)
            )
        }
    }
}