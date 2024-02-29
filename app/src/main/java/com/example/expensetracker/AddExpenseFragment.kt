package com.example.expensetracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.expensetracker.database.Expense
import com.example.expensetracker.databinding.FragmentAddExpenseBinding
import java.util.*

class AddExpenseFragment : Fragment() {
    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!

    private val expenseListViewModel: ExpenseListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // Set an onItemSelectedListener for spinnerSetCategory to handle category selection
            // Initialize the spinner with categories using an ArrayAdapter

            goBack.setOnClickListener { // Make sure to use the correct ID for your add button
                val titleText = setTitle.text.toString()
                val amountText = setExpense.text.toString()
                val amount = amountText.toDoubleOrNull()
                val category = spinnerSetCategory.selectedItem.toString()

                // Log the data that is being attempted to add
                Log.d("ADDEXPENSEFRAGMENT", "Attempting to add expense with title: $titleText, amount: $amountText, category: $category")

                if (amount != null) {
                    val newExpense = Expense(UUID.randomUUID(), titleText, Date(), amount, category)
                    expenseListViewModel.addExpense(newExpense)
                    Log.d("ADDEXPENSEFRAGMENT", "Expense added: $newExpense")
                } else {
                    // Log an error if the amount is not valid
                    Log.e("ADDEXPENSEFRAGMENT", "Error adding expense: Amount is not a valid number")
                }

                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}