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
import kotlinx.coroutines.launch
import java.util.Date


class ExpenseListViewModel(application: Application) : AndroidViewModel(application) {

    // Assuming your Room database is named AppDatabase and it has a method to build the database instance.
    private val db = Room.databaseBuilder(application, ExpenseDatabase::class.java, "expenses").build()
    private val expenseDao = db.ExpenseDao()

    // LiveData list of expenses
    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses().asLiveData()

    // Functions to fetch data by category
    fun getExpensesByCategory(category: String): LiveData<List<Expense>> {
        return expenseDao.getExpensesByCategory(category).asLiveData()
    }

    // Function to fetch all expenses
    fun fetchAllExpenses(): LiveData<List<Expense>> {
        return allExpenses // Already initialized as LiveData
    }

    // Function to add an expense
    fun addExpense(expense: Expense) = viewModelScope.launch {
        expenseDao.insertExpense(expense)
    }

    // Function to update an expense
    fun updateExpense(expense: Expense) = viewModelScope.launch {
        expenseDao.updateExpense(expense)
    }

    // Function to delete all expenses
    fun deleteAllExpenses() = viewModelScope.launch {
        expenseDao.deleteAllExpenses()
    }

    override fun onCleared() {
        super.onCleared()
        db.close() // Close the database when the ViewModel is cleared
    }
}