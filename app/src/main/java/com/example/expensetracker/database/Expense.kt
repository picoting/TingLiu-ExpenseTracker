package com.example.expensetracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="expenses")
data class Expense(
    @PrimaryKey val id: UUID,
    val title: String,
    val date: Date,
    val amount: Double,
    val category: String
)