package com.abdullahhalis.expensetracker.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullahhalis.expensetracker.data.local.ExpenseEntity
import com.abdullahhalis.expensetracker.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
): ViewModel() {
    private val _expense = MutableStateFlow<ExpenseEntity?>(null)
    val expense = _expense.asStateFlow()

    fun loadExpense(id: Long) {
        viewModelScope.launch {
            repository.getExpenseById(id).collect { expense ->
                expense?.let { _expense.value = it }
            }
        }
    }

    fun deleteExpense(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteExpenseById(id)
            onSuccess()
        }
    }
}