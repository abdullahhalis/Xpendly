package com.abdullahhalis.expensetracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun  deleteExpenseById(id: Long)

    @Query("SELECT * FROM expenses ORDER BY dateInMillis DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Long): Flow<ExpenseEntity?>

    @Query("""
        SELECT * FROM expenses
        WHERE dateInMillis BETWEEN :startDate AND :endDate
        ORDER BY dateInMillis DESC
    """)
    fun getExpenseByDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpense(): Flow<Double?>

    @Query("""
        SELECT SUM(amount) FROM expenses
        WHERE dateInMillis BETWEEN :startDate AND :endDate
    """)
    fun getTotalExpenseByDateRange(startDate: Long, endDate: Long): Flow<Double?>
}