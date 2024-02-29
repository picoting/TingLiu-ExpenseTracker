package com.example.expensetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.database.Expense
import java.text.SimpleDateFormat
import java.util.*

class ExpenseListAdapter : ListAdapter<Expense, ExpenseListAdapter.ExpenseViewHolder>(ExpensesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        return ExpenseViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val expenseTitleView: TextView = itemView.findViewById(R.id.expense_title)
        private val expenseCategoryView: TextView = itemView.findViewById(R.id.expense_category)
        private val expenseAmountView: TextView = itemView.findViewById(R.id.expense_amount)
        private val expenseDateView: TextView = itemView.findViewById(R.id.expense_date)

        fun bind(expense: Expense) {
            expenseTitleView.text = expense.title
            expenseCategoryView.text = expense.category
            expenseAmountView.text = String.format("$%.2f", expense.amount)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            expenseDateView.text = sdf.format(expense.date)
        }

        companion object {
            fun create(parent: ViewGroup): ExpenseViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_expense, parent, false)
                return ExpenseViewHolder(view)
            }
        }
    }

    class ExpensesComparator : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}