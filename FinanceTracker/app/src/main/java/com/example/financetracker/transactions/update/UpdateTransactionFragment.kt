package com.example.financetracker.transactions.update

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.financetracker.R
import com.example.financetracker.model.Transaction
import com.example.financetracker.viewmodel.TransactionViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class UpdateTransactionFragment : Fragment() {
    private lateinit var transactionVM: TransactionViewModel
    private val args by navArgs<UpdateTransactionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_transaction, container, false)

        // get transaction view model
        transactionVM = ViewModelProvider(this)[TransactionViewModel::class.java]

        // Get current transaction data
        view.let {
            val nameView = it.findViewById<TextInputEditText>(R.id.name_edit)
            val categoryView = it.findViewById<TextInputEditText>(R.id.category_edit)
            val dateView = it.findViewById<TextInputEditText>(R.id.date_edit)
            val amountView = it.findViewById<TextInputEditText>(R.id.amount_edit)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")

            // preset fields for update screen
            nameView.setText(args.curTransaction.name)
            categoryView.setText(args.curTransaction.category)
            dateView.setText(dateFormat.format(args.curTransaction.date))
            amountView.setText(abs(args.curTransaction.amount).toString())
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
                val date = dateFormat.parse(dateView.text.toString()).time
                val amount = getAmountFromType(
                    view,
                    amountView.text.toString().toFloat()
                )
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
                    var date = dateFormat.parse(dateView.text.toString()).time
                    val amount = getAmountFromType(
                        view,
                        amountView.text.toString().toFloat()
                    )
                    val transaction = Transaction(args.curTransaction.id, name, date, amount, category)
                    transactionVM.updateTransaction(transaction)
                    Toast.makeText(requireContext(), "Updated transaction successfully!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_updateTransactionFragment_to_transactionListsFragment)
                }
            }

            // set listener for delete button
            it.findViewById<Button>(R.id.delete_btn).setOnClickListener {
                var alertDialog = AlertDialog.Builder(requireContext())
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