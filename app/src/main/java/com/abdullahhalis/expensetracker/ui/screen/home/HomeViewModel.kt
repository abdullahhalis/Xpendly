package com.abdullahhalis.expensetracker.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullahhalis.expensetracker.data.local.ExpenseEntity
import com.abdullahhalis.expensetracker.data.repository.ExpenseRepository
import com.abdullahhalis.expensetracker.ui.utils.DateOption
import com.abdullahhalis.expensetracker.ui.utils.toDateRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ExpenseRepository
): ViewModel() {
    private val _selectedFilter = MutableStateFlow(DateOption.MONTH)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _selectedListFilter = MutableStateFlow(DateOption.All)
    val selectedListFilter = _selectedListFilter.asStateFlow()

    val expenses: StateFlow<List<ExpenseEntity>> = _selectedListFilter
        .flatMapLatest { filter ->
            val (start, end) = filter.toDateRange()
            if (filter == DateOption.All) repository.getAllExpenses()
            else repository.getExpenseByDate(start, end)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalExpense: StateFlow<Double> = _selectedFilter
        .flatMapLatest { filter ->
            val (start, end) = filter.toDateRange()
            if (filter == DateOption.All) repository.getTotalExpense()
            else repository.getTotalExpenseByDateRange(start, end)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun onFilterSelected(filter: DateOption) {
        _selectedFilter.value = filter
    }

    fun onListFilterSelected(filter: DateOption) {
        _selectedListFilter.value = filter
    }
}