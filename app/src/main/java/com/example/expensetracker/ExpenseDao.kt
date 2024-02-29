package com.example.expensetracker

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*
import com.example.expensetracker.Expense

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdateExpense(expense: Expense): Long

    @Query("SELECT COUNT(*) FROM Expense")
    suspend fun getExpenseCount(): Int

    @Query("SELECT * FROM Expense ORDER BY date DESC")
    fun getExpensesSortedByDate(descending: Boolean = true): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE category = :category ORDER BY date DESC")
    suspend fun getExpensesByCategory(category: String): List<Expense>

    @Update
    suspend fun updateExpense(expense: Expense)

    @Query("SELECT * FROM Expense WHERE id = :id")
    suspend fun getExpenseById(id: UUID): Expense

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("DELETE FROM Expense")
    suspend fun deleteAllExpenses()

    @Query("UPDATE Expense SET amount = :newAmount, category = :newCategory, date = :newDate WHERE id = :id")
    suspend fun updateExpenseDetails(id: UUID, newAmount: Double, newCategory: String, newDate: Date)
}