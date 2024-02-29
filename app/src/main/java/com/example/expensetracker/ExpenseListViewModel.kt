package com.example.expensetracker

import android.app.Application
import android.util.Log
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
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID


class ExpenseListViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(application, ExpenseDatabase::class.java, "expenses").build()
    private val expenseDao: ExpenseDao = db.expenseDao()

    private val _expensesByCategory = MutableLiveData<List<Expense>>()
    val expensesByCategory: LiveData<List<Expense>> = _expensesByCategory


    private val _expenseList = MutableLiveData<List<Expense>>()
    val allExpenses: LiveData<List<Expense>> = _expenseList

    fun fetchExpensesByCategory(category: String) = viewModelScope.launch {
        val expenses = expenseDao.getExpensesByCategory(category)
        _expensesByCategory.value = expenses
    }

    fun sortExpenseByDate() = viewModelScope.launch {
        expenseDao.getExpensesSortedByDate()
    }


    fun addExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) { // <-- Dispatchers.IO added here
        try {
            Log.d("ExpenseListViewModel", "addExpense called with: $expense")
            val newRowId = expenseDao.addExpense(expense)
            if (newRowId > -1) {
                Log.d("AddExpense", "Expense added successfully, ID: $newRowId")
            } else {
                Log.d("AddExpense", "Failed to add expense")
            }
        } catch (e: Exception) {
            Log.e("ExpenseListViewModel", "Exception adding expense", e)
        }

        //expenseDao.addExpense(expense)
        // Now collect the latest list of expenses and post the value to the LiveData
        val expensesList = expenseDao.getExpensesSortedByDate().first()
        withContext(Dispatchers.Main) {
            _expenseList.postValue(listOf(expensesList))
        }
    }

    fun updateExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        expenseDao.updateExpense(expense)
    }

    fun deleteAllExpenses() = viewModelScope.launch {
        expenseDao.deleteAllExpenses()
    }

    fun getExpenseById(expenseId: UUID): LiveData<Expense> {
        val result = MutableLiveData<Expense>()
        viewModelScope.launch {
            result.value = expenseDao.getExpenseById(expenseId)
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        db.close() // Close the database when the ViewModel is cleared
    }
}