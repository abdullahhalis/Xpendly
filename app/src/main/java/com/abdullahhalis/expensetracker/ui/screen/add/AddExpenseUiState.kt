package com.abdullahhalis.expensetracker.ui.screen.add

import com.abdullahhalis.expensetracker.ui.utils.MyCategory

data class AddExpenseUiState(
    val title: String = "",
    val amount: String = "",
    val note: String = "",
    val category: MyCategory = MyCategory.OTHER,
    val dateInMillis: Long = System.currentTimeMillis()
)
