package com.abdullahhalis.expensetracker.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullahhalis.expensetracker.data.local.ExpenseEntity
import com.abdullahhalis.expensetracker.data.repository.ExpenseRepository
import com.abdullahhalis.expensetracker.ui.screen.add.AddExpenseUiState
import com.abdullahhalis.expensetracker.ui.utils.MyCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
) : ViewModel() {
    private val _expense = MutableStateFlow<ExpenseEntity?>(null)
    val expense = _expense.asStateFlow()

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState = _uiState.asStateFlow()

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title, titleError = null) }
    }

    fun onAmountChange(amount: String) {
        val filtered = amount.trimStart('0')
        _uiState.update { it.copy(amount = filtered, amountError = null) }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onDateChange(dateInMillis: Long) {
        _uiState.update { it.copy(dateInMillis = dateInMillis) }
    }

    fun onCategoryChange(category: MyCategory) {
        _uiState.update { it.copy(category = category) }
    }

    fun loadExpense(id: Long) {
        viewModelScope.launch {
            repository.getExpenseById(id).collect { expenseEntity ->
                expenseEntity?.let { expense ->
                    _expense.value = expense
                    _uiState.update {
                        it.copy(
                            title = expense.title,
                            amount = expense.amount.toInt().toString(),
                            note = expense.note,
                            category = MyCategory.getByLabel(expense.category),
                            dateInMillis = expense.dateInMillis
                        )
                    }
                }
            }
        }
    }

    fun deleteExpense(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteExpenseById(id)
            onSuccess()
        }
    }

    fun updateExpense(onSuccess: () -> Unit) {
        val state = _uiState.value

        var hasError = false

        if (state.title.isBlank()) {
            _uiState.update { it.copy(titleError = "Title cannot be empty") }
            hasError = true
        }

        if (state.amount.isBlank() || (state.amount.toDoubleOrNull() ?: 0.0) <= 0) {
            _uiState.update { it.copy(amountError = "Amount must be greater than 0") }
            hasError = true
        }

        if (hasError) return

        val updatedExpense = expense.value?.copy(
            title = state.title,
            amount = state.amount.toDoubleOrNull() ?: 0.0,
            category = state.category.label,
            note = state.note,
            dateInMillis = state.dateInMillis
        )

        updatedExpense?.let {
            viewModelScope.launch {
                repository.updateExpense(updatedExpense)
                onSuccess()
            }
        }
    }

    fun cancelUpdate() {
        _expense.value?.let { expense ->
            _uiState.update {
                it.copy(
                    title = expense.title,
                    amount = expense.amount.toInt().toString(),
                    note = expense.note,
                    category = MyCategory.getByLabel(expense.category),
                    dateInMillis = expense.dateInMillis,
                    amountError = null,
                    titleError = null
                )
            }
        }
    }
}