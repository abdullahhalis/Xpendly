package com.abdullahhalis.expensetracker.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdullahhalis.expensetracker.R
import com.abdullahhalis.expensetracker.data.local.ExpenseEntity
import com.abdullahhalis.expensetracker.ui.components.DateDropDown
import com.abdullahhalis.expensetracker.ui.components.ExpenseItem
import com.abdullahhalis.expensetracker.ui.components.TotalExpenseCard
import com.abdullahhalis.expensetracker.ui.navigation.Screen
import com.abdullahhalis.expensetracker.ui.theme.ExpenseTrackerTheme
import com.abdullahhalis.expensetracker.ui.utils.DateOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val selectedListFilter by viewModel.selectedListFilter.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarMessage = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("snackbar_message", null)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarMessage?.value) {
        snackbarMessage?.value?.let { message ->
            snackbarHostState.showSnackbar(message)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("snackbar_message", null)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddExpense.route)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "add expense",
                )
            }
        },
    ) { contentPadding ->
        HomeContent(
            expenses,
            totalExpense = totalExpense,
            selectedFilter = selectedFilter,
            selectedListFilter = selectedListFilter,
            onFilterSelected = viewModel::onFilterSelected,
            onListFilterSelected = viewModel::onListFilterSelected,
            navigateToDetail = { id ->
              navController.navigate(Screen.Detail.createRoute(id))
            },
            modifier.padding(contentPadding)
        )
    }
}

@Composable
fun HomeContent(
    expenses: List<ExpenseEntity>,
    totalExpense: Double,
    selectedFilter: DateOption,
    selectedListFilter: DateOption,
    onFilterSelected: (DateOption) -> Unit,
    onListFilterSelected: (DateOption) -> Unit,
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            end = 12.dp,
            top = 12.dp,
            bottom = 80.dp
        ),
        modifier = modifier
    ) {
        item {
            TotalExpenseCard(
                totalExpense,
                dateOption = selectedFilter,
                onOptionSelected = onFilterSelected,
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                Text(
                    "Recent Expenses",
                    fontWeight = FontWeight.SemiBold
                )

                DateDropDown(
                    selectedListFilter,
                    onOptionSelected = onListFilterSelected
                )
            }
        }

        if (expenses.isEmpty()) {
            val emptyMessage = when(selectedListFilter) {
                DateOption.DAY -> "No expenses today"
                DateOption.WEEK -> "No expenses this week"
                DateOption.MONTH -> "No expenses this month"
                DateOption.YEAR -> "No expenses this year"
                else -> "No expenses yet"
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        emptyMessage,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        } else {
            items(expenses, key = { expense -> expense.id}) { expense ->
                ExpenseItem(
                    expense,
                    { navigateToDetail(expense.id) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPrev() {
    ExpenseTrackerTheme(
        darkTheme = false
    ) {
        HomeScreen(
            rememberNavController()
        )
    }
}

val dummyExpenses = listOf(
    ExpenseEntity(
        id = 0,
        title = "Gitar Akustik",
        amount = 200000.0,
        category = "Other",
        note = "belajar gitar",
        dateInMillis = System.currentTimeMillis() - 3 * 86_400_000L
    ),
    ExpenseEntity(
        id = 1,
        title = "Starbucks Coffee",
        amount = 65000.0,
        category = "Food & Drink",
        note = "",
        dateInMillis = System.currentTimeMillis()
    ),
    ExpenseEntity(
        id = 2,
        title = "Gojek Ride",
        amount = 24000.0,
        category = "Transport",
        note = "",
        dateInMillis = System.currentTimeMillis() - 86_400_000L // yesterday
    ),
    ExpenseEntity(
        id = 3,
        title = "Netflix",
        amount = 54000.0,
        category = "Entertainment",
        note = "",
        dateInMillis = System.currentTimeMillis() - 86_400_000L
    ),
    ExpenseEntity(
        id = 4,
        title = "Indomaret",
        amount = 45000.0,
        category = "Shopping",
        note = "",
        dateInMillis = System.currentTimeMillis() - 2 * 86_400_000L
    ),
    ExpenseEntity(
        id = 5,
        title = "Listrik PLN",
        amount = 250000.0,
        category = "Bill",
        note = "",
        dateInMillis = System.currentTimeMillis() - 3 * 86_400_000L
    ),
)