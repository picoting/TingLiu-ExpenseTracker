package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensetracker.database.Expense
import com.example.expensetracker.databinding.FragmentEditExpenseBinding
import java.util.*

class EditExpenseFragment : Fragment() {
    private var _binding: FragmentEditExpenseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpenseListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assuming you have an array resource for categories
        val categoriesAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.expense_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSetCategory.adapter = adapter
        }

        // Load the current expense details
        val args = requireArguments()
        val expenseIdString = args.getString("expenseId")
        val expenseId = UUID.fromString(expenseIdString)
        loadExpenseDetails(expenseId)

        // The ID passed to the fragment
        loadExpenseDetails(expenseId)

        binding.apply {

            // Handle the go back action
            goBack.setOnClickListener {
                val newTitle = editTitle.text.toString().toString()
                val newAmount = updateExpense.text.toString().toDoubleOrNull()
                val newCategory = spinnerSetCategory.selectedItem.toString()
                val newDate = Date() // Use a date picker or other input mechanism for the date

                if (newAmount != null) {
                    val updatedExpense = Expense(expenseId, newTitle, newDate, newAmount, newCategory)
                    viewModel.updateExpense(updatedExpense)
                } else {
                    // Show an error message if the amount is not valid
                }

                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun loadExpenseDetails(expenseId: UUID) {
        viewModel.getExpenseById(expenseId).observe(viewLifecycleOwner) { expense ->
            binding.apply {
                editTitle.setText(expense.title) // Assuming you have a title in your Expense model
                updateExpense.setText(expense.amount.toString())
                // Assuming you have a method to find the position of the category in the spinner
                val categoryPosition = (spinnerSetCategory.adapter as ArrayAdapter<String>).getPosition(expense.category)
                spinnerSetCategory.setSelection(categoryPosition)

                // Date handling (if you have a DatePicker or similar, you'll need to set it here)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}