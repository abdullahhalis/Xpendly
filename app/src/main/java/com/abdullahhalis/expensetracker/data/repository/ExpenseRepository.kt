package com.abdullahhalis.expensetracker.data.repository

import com.abdullahhalis.expensetracker.data.local.ExpenseDao
import com.abdullahhalis.expensetracker.data.local.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    suspend fun insertExpense(expense: ExpenseEntity) = expenseDao.insertExpense(expense)

    suspend fun deleteExpenseById(id: Long) = expenseDao.deleteExpenseById(id)

    fun getExpenseById(id: Long): Flow<ExpenseEntity?> = expenseDao.getExpenseById(id)

    fun getAllExpenses(): Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()

    fun getExpenseByDate(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpenseByDateRange(startDate, endDate)

    fun getTotalExpense(): Flow<Double> = expenseDao.getTotalExpense().map { it ?: 0.0 }

    fun getTotalExpenseByDateRange(startDate: Long, endDate: Long): Flow<Double> =
        expenseDao.getTotalExpenseByDateRange(startDate, endDate).map { it ?: 0.0 }

}