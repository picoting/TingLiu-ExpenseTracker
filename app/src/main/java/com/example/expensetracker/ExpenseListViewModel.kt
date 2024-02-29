package com.example.expensetracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.expensetracker.database.Expense
import com.example.expensetracker.database.ExpenseDao
import com.example.expensetracker.database.ExpenseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


class ExpenseListViewModel(application: Application) : AndroidViewModel(application) {

    // Assuming your Room database is named AppDatabase and it has a method to build the database instance.
    private val db = Room.databaseBuilder(application, ExpenseDatabase::class.java, "expenses").build()
    private val expenseDao: ExpenseDao = db.expenseDao()

    private val _expensesByCategory = MutableLiveData<List<Expense>>()
    val expensesByCategory: LiveData<List<Expense>> = _expensesByCategory


    val allExpenses: Flow<List<Expense>> = expenseDao.getExpensesSortedByDate()

    fun fetchExpensesByCategory(category: String) = viewModelScope.launch {
        val expenses = expenseDao.getExpensesByCategory(category)
        _expensesByCategory.value = expenses
    }


    fun addExpense(expense: Expense) = viewModelScope.launch {
        expenseDao.addExpense(expense)
    }

    fun updateExpense(id: UUID, newAmount: Double, newCategory: String, newDate: Date) = viewModelScope.launch {
        expenseDao.updateExpenseDetails(id, newAmount, newCategory, newDate)
    }

    fun deleteAllExpenses() = viewModelScope.launch {
        expenseDao.deleteAllExpenses()
    }

    override fun onCleared() {
        super.onCleared()
        db.close() // Close the database when the ViewModel is cleared
    }
}