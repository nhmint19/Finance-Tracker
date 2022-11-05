package com.example.financetracker.view.export

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.financetracker.R
import com.example.financetracker.viewmodel.TransactionViewModel
import com.example.financetracker.utils.createExcel
import com.example.financetracker.utils.getFileUri

class ExportFragment : Fragment() {
    private lateinit var transactionVM : TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_export, container, false)
        transactionVM = ViewModelProvider(this)[TransactionViewModel::class.java]
        transactionVM.readAllTransactions.observe(viewLifecycleOwner) { transaction ->
            run {
                view.findViewById<Button>(R.id.export_btn).setOnClickListener {
                    createExcel(requireContext(), "Transactions.xls", "Transaction", transaction)
                    val uri = getFileUri(requireContext(), "Transactions.xls")
                    launchShareIntent(uri)
                }
            }
        }
        return view
    }

    private fun launchShareIntent(uri: Uri?) {
        val intent = ShareCompat.IntentBuilder(requireContext())
            .setType("application/pdf")
            .setStream(uri)
            .setChooserTitle("Select application to share file")
            .createChooserIntent()
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }
}