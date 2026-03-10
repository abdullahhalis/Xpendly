package com.abdullahhalis.expensetracker.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullahhalis.expensetracker.data.local.ExpenseEntity
import com.abdullahhalis.expensetracker.data.repository.ExpenseRepository
import com.abdullahhalis.expensetracker.ui.utils.MyCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    val repository: ExpenseRepository
): ViewModel() {
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

    fun saveExpense(onSuccess: () -> Unit) {
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

        viewModelScope.launch {
            repository.insertExpense(
                ExpenseEntity(
                    title = state.title,
                    amount = state.amount.toDoubleOrNull() ?: 0.0,
                    category = state.category.label,
                    note = state.note,
                    dateInMillis = state.dateInMillis
                )
            )
            onSuccess()
        }
    }
}