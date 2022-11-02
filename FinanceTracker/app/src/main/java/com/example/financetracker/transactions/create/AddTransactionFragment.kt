package com.example.financetracker.transactions.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.financetracker.CategoryDropdownAdapter
import com.example.financetracker.R
import com.example.financetracker.data.model.Category
import com.example.financetracker.data.model.Transaction
import com.example.financetracker.data.viewmodel.CategoryViewModel
import com.example.financetracker.data.viewmodel.TransactionViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class AddTransactionFragment : Fragment() {
    private lateinit var transactionVM: TransactionViewModel
    private lateinit var categoryVM: CategoryViewModel
    private var categories = emptyList<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_transaction, container, false)

        val nameView = view.findViewById<TextInputEditText>(R.id.name_add)
        val categoryView = view.findViewById<AutoCompleteTextView>(R.id.category_add)
        val dateView = view.findViewById<TextInputEditText>(R.id.date_add)
        val amountView = view.findViewById<TextInputEditText>(R.id.amount_add)

        // get transaction view model
        transactionVM = ViewModelProvider(this)[TransactionViewModel::class.java]

        // date input click listener
        dateView.setOnClickListener {
            onClickDateInput(dateView)
        }

        // add button click listener
        view.findViewById<Button>(R.id.add_btn).setOnClickListener {
            val name = nameView.text.toString()
            val category = categoryView.text.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")

            if (TextUtils.isEmpty(name))
                nameView.error = "Name must not be empty"
            else if (TextUtils.isEmpty(category))
                categoryView.error = "Category must not be empty"
            else if (TextUtils.isEmpty(dateView.text)) {
                dateView.error = "Date must not be empty"
            }
            else if (TextUtils.isEmpty(amountView.text))
                amountView.error = "Amount must not be empty"
            else if (amountView.text.toString().toFloat() == 0F)
                amountView.error = "Amount must be greater than 0"
            else if (
                !view.findViewById<RadioButton>(R.id.radio_expense).isChecked &&
                !view.findViewById<RadioButton>(R.id.radio_income).isChecked
            )
                amountView.error = "Please select a type of transaction"
            else
            {
                var date = dateFormat.parse(dateView.text.toString()).time
                val amount = getAmountFromType(
                    view,
                    amountView.text.toString().toFloat()
                )
                val transaction = Transaction(0, name, date, amount, category)
                transactionVM.addTransaction(transaction)
                Toast.makeText(requireContext(), "Added transaction successfully!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_addTransactionFragment_to_transactionListsFragment)
            }
        }

        // Category drop down
        categoryVM = ViewModelProvider(this)[CategoryViewModel::class.java]
        categoryVM.readAllCategories.observe(viewLifecycleOwner, Observer {
            categoryList ->
            run {
                categories = categoryList
                val adapter = ArrayAdapter( requireContext(), R.layout.dropdown_category, categories.map { category -> category.name})
                view.findViewById<AutoCompleteTextView>(R.id.category_add).setAdapter(adapter)
            }
        })
        return view
    }

    // date input click event
    private fun onClickDateInput(view: TextInputEditText) {
        // get instance of calendar
        val c = Calendar.getInstance()

        // get year, month, day
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // create date picker dialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                // set date
                val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                view.setText(dat)
            },
            year,
            month,
            day
        )
        // display dialog
        datePickerDialog.show()
    }

    // update the amount based on transaction type
    private fun getAmountFromType(view: View, amount: Float) : Float {
        if (view.findViewById<RadioButton>(R.id.radio_expense).isChecked) {
            return (-amount)
        }
        return amount
    }
}