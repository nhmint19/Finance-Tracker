package com.example.financetracker.view.transactions

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.financetracker.R
import com.example.financetracker.api.DEFAULT_CURRENCY
import com.example.financetracker.api.DEFAULT_VALUE
import com.example.financetracker.model.Category
import com.example.financetracker.model.Transaction
import com.example.financetracker.viewmodel.CategoryViewModel
import com.example.financetracker.viewmodel.TransactionViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class UpdateTransactionFragment : Fragment() {
    private lateinit var transactionVM: TransactionViewModel
    private lateinit var categoryVM: CategoryViewModel
    private var categories = emptyList<Category>()
    private val args by navArgs<UpdateTransactionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // get settings
        val prefs = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val currencyCode = prefs?.getString("currency_code", DEFAULT_CURRENCY).toString()
        val currencyValue = prefs?.getString("currency_value", DEFAULT_VALUE)?.toFloat()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_transaction, container, false)

        // get transaction view model
        transactionVM = ViewModelProvider(this)[TransactionViewModel::class.java]

        // Get current transaction data
        view.let {
            val nameView = it.findViewById<TextInputEditText>(R.id.name_edit)
            val categoryView = it.findViewById<AutoCompleteTextView>(R.id.category_edit)
            val dateView = it.findViewById<TextInputEditText>(R.id.date_edit)
            val amountView = it.findViewById<TextInputEditText>(R.id.amount_edit)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")

            // update the currency text
            view.findViewById<TextView>(R.id.amount_edit_currency).text = currencyCode

            // preset fields for update screen
            nameView.setText(args.curTransaction.name)
            categoryView.setText(args.curTransaction.category)
            dateView.setText(dateFormat.format(args.curTransaction.date))
            amountView.setText(abs(args.curTransaction.amount * currencyValue!!).toString())
            if (args.curTransaction.amount > 0) {
                it.findViewById<RadioButton>(R.id.radio_income).isChecked = true
            } else {
                it.findViewById<RadioButton>(R.id.radio_expense).isChecked = true
            }

            // date input click listener
            dateView.setOnClickListener {
                onClickDateInput(dateView)
            }

            // set listener for update button
            it.findViewById<Button>(R.id.update_btn).setOnClickListener {
                val name = nameView.text.toString()
                val category = categoryView.text.toString()
                if (TextUtils.isEmpty(name))
                    nameView.error = "Name must not be empty"
                else if (TextUtils.isEmpty(category))
                    categoryView.error = "Category must not be empty"
                else if (TextUtils.isEmpty(dateView.text))
                    dateView.error = "Date must not be empty"
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
                    val date = dateFormat.parse(dateView.text.toString())!!.time
                    val amount = getAmountFromType(
                        view,
                        amountView.text.toString().toFloat()
                    )
                    val transaction = Transaction(args.curTransaction.id, name, date, amount / currencyValue, category)
                    transactionVM.updateTransaction(transaction)
                    Toast.makeText(requireContext(), "Updated transaction successfully!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_updateTransactionFragment_to_transactionListsFragment)
                }
            }

            // set listener for delete button
            it.findViewById<Button>(R.id.delete_btn).setOnClickListener {
                // create a confirmation dialog
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Delete ${args.curTransaction.name}?")
                alertDialog.setMessage("Are you sure you want to delete ${args.curTransaction.name}?")
                alertDialog.setPositiveButton("Yes") {_, _ ->
                    transactionVM.deleteTransaction(args.curTransaction)
                    Toast.makeText(requireContext(), "Deleted transaction successfully!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_updateTransactionFragment_to_transactionListsFragment)
                }
                alertDialog.setNegativeButton("No") {_, _ ->}
                alertDialog.create().show()
            }
        }

        // Category drop down
        categoryVM = ViewModelProvider(this)[CategoryViewModel::class.java]
        categoryVM.readAllCategories.observe(viewLifecycleOwner) { categoryList ->
            run {
                categories = categoryList
                val categoriesNames = mutableListOf<String>()
                for (category in categories) {
                    categoriesNames.add(category.name)
                }
                categoriesNames.add("Other")
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_category,
                    categoriesNames)
                view.findViewById<AutoCompleteTextView>(R.id.category_edit).setAdapter(adapter)
            }
        }

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
            { _, _year, monthOfYear, dayOfMonth ->
                // set date
                val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + _year)
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