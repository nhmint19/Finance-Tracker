package com.example.financetracker.view.transactions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financetracker.R
import com.example.financetracker.api.DEFAULT_CURRENCY
import com.example.financetracker.api.DEFAULT_VALUE
import com.example.financetracker.viewmodel.TransactionViewModel

class TransactionListsFragment : Fragment() {
    private lateinit var transactionVM : TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // get settings
        val prefs = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val currencyCode = prefs?.getString("currency_code", DEFAULT_CURRENCY).toString()
        val currencyValue = prefs?.getString("currency_value", DEFAULT_VALUE)?.toFloat()

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
        transactionVM.readAllTransactions.observe(viewLifecycleOwner) { transaction ->
            adapter.setData(transaction)
            // set text for expense, income and saving total
            val (expense, income, total) = adapter.calculateTotal()
            if (total >= 0) {
                totalView.text = "${totalView.text}\n${currencyCode} ${
                    String.format(
                        "%.2f",
                        total * currencyValue!!
                    )
                }"
                totalView.setTextColor(
                    ResourcesCompat.getColor(
                        view.resources,
                        R.color.green,
                        null
                    )
                )
            } else {
                totalView.text = "${totalView.text}\n-${currencyCode} ${
                    String.format(
                        "%.2f",
                        -total * currencyValue!!
                    )
                }"
                totalView.setTextColor(ResourcesCompat.getColor(view.resources, R.color.red, null))
            }
            expenseView.text = "${expenseView.text}\n${currencyCode} ${
                String.format(
                    "%.2f",
                    expense * currencyValue
                )
            }"
            incomeView.text = "${incomeView.text}\n${currencyCode} ${
                String.format(
                    "%.2f",
                    income * currencyValue
                )
            }"
        }

        // set listener to add icon
        view.findViewById<ImageView>(R.id.ic_add_transactions).setOnClickListener {
            findNavController().navigate(R.id.action_transactionListsFragment_to_addTransactionFragment)
        }

        return view
    }
}