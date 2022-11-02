package com.example.financetracker.transactions.read

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financetracker.CategoryDropdownAdapter
import com.example.financetracker.R
import com.example.financetracker.TransactionAdapter
import com.example.financetracker.data.model.Category
import com.example.financetracker.data.model.Transaction
import com.example.financetracker.data.viewmodel.CategoryViewModel
import com.example.financetracker.data.viewmodel.TransactionViewModel
import com.example.financetracker.databinding.FragmentTransactionListsBinding

class TransactionListsFragment : Fragment() {
    private lateinit var transactionVM : TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction_lists, container, false)
        val totalView = view.findViewById<TextView>(R.id.total)
        val expenseView = view.findViewById<TextView>(R.id.expense)
        val incomeView = view.findViewById<TextView>(R.id.income)

        // set up Recycler View
        val transactionList = view.findViewById<RecyclerView>(R.id.transactions_list)
        val adapter = TransactionAdapter()
        transactionList.adapter = adapter
        transactionList.layoutManager = LinearLayoutManager(requireContext())

        // TransactionVM
        transactionVM = ViewModelProvider(this)[TransactionViewModel::class.java]
        transactionVM.readAllTransactions.observe(viewLifecycleOwner, Observer {
            transaction -> adapter.setData(transaction)
            // set text for expense, income and saving total
            val (expense, income, total) = adapter.calculateTotal()
            if (total >= 0) {
                totalView.text = "${totalView.text}\n$${total}"
                totalView.setTextColor(ResourcesCompat.getColor(view.resources, R.color.green, null))
            } else {
                totalView.text = "${totalView.text}\n-$${-total}"
                totalView.setTextColor(ResourcesCompat.getColor(view.resources, R.color.red, null))
            }
            expenseView.text = "${expenseView.text}\n$${expense}"
            incomeView.text = "${incomeView.text}\n$${income}"
        })

        // set listener to add icon
        view.findViewById<ImageView>(R.id.ic_add_transactions).setOnClickListener {
            findNavController().navigate(R.id.action_transactionListsFragment_to_addTransactionFragment)
        }

        return view
    }
}