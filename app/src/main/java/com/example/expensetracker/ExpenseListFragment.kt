package com.example.expensetracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.FragmentExpenseListBinding

private const val TAG = "ExpenseListFragment"

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!

    private val expenseListViewModel: ExpenseListViewModel by viewModels()
    private lateinit var expenseListAdapter: ExpenseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSpinner()
        setupAddExpenseButton()
    }

    private fun setupRecyclerView() {
        expenseListAdapter = ExpenseListAdapter()
        binding.recyclerViewExpenses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = expenseListAdapter
        }

        // Observe the LiveData from ViewModel
        expenseListViewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            expenseListAdapter.submitList(expenses)
        }
    }

    private fun setupSpinner() {
        val categorySpinner = binding.spinnerSortCategories

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // get category from the spinner
                if (position > 0) {
                    val selectedCategory = parent?.getItemAtPosition(position).toString()
                    //  fetch news from category using viewmodel?
                    Log.d(
                        "Expense List Fragment",
                        "Category changed to: $selectedCategory"
                    )
                    expenseListViewModel.fetchExpensesByCategory(selectedCategory)
                }
                else {
                    expenseListViewModel.sortExpenseByDate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do nothing?
            }
        }
    }

    private fun setupAddExpenseButton() {
        binding.addExpense.setOnClickListener {
            val addExpenseFragment = AddExpenseFragment()

            // Use the parentFragmentManager to begin a transaction
            parentFragmentManager.beginTransaction().apply {
                // Replace the current fragment with the AddExpenseFragment instance
                replace(R.id.nav_host_fragment, addExpenseFragment)

                // Optionally add the transaction to the back stack
                // This enables the user to navigate back to the previous fragment using the device's back button
                addToBackStack(null)

                // Commit the transaction
                commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}