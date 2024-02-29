package com.example.expensetracker.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*
import com.example.expensetracker.database.Expense

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExpense(expense: Expense): Long

    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun getExpenseCount(): Int

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getExpensesSortedByDate(descending: Boolean = true): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    suspend fun getExpensesByCategory(category: String): List<Expense>

    @Update
    suspend fun updateExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: UUID): Expense

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("DELETE FROM expenses")
    suspend fun deleteAllExpenses()

    @Query("UPDATE expenses SET amount = :newAmount, category = :newCategory, date = :newDate WHERE id = :id")
    suspend fun updateExpenseDetails(id: UUID, newAmount: Double, newCategory: String, newDate: Date)
}